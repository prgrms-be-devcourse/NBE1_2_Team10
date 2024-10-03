package core.application.filter;

import core.application.Util.JwtUtil;
import core.application.users.service.CustomUserDetails;
import core.application.users.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;  // 추가된 부분
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public CustomLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        setFilterProcessesUrl("/users/signin");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // InputStream에서 JSON 문자열을 UTF-8로 읽어옴
            String jsonInput = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // JSON 객체 생성
            JSONObject jsonObject = new JSONObject(jsonInput);

            // userEmail과 userPw 추출
            String userEmail = jsonObject.getString("userEmail");
            String userPw = jsonObject.getString("userPw");

            //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, userPw, null);

            //token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            // 인증 과정에서 발생한 예외를 로그로 남김
            System.err.println("Authentication failed: " + e.getMessage());
            throw e; // 예외를 다시 던져서 로그인 실패로 처리
        } catch (IOException | JSONException e) {
            // JSON 파싱 또는 IO 오류 처리
            System.err.println("Invalid request format: " + e.getMessage());
            throw new AuthenticationException("Invalid request format") {};
        }
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(14*24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String userEmail = customUserDetails.getUserEmail();
        UUID userId = customUserDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 기한 설정 // 추후 수정
        String accessToken = jwtUtil.creatAccessToken(userEmail, userId, role, "access");
        String refreshToken = jwtUtil.creatRefreshToken(userEmail, "refresh");

        response.setHeader("accessToken", accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        redisService.setValueWithTTL(userEmail, refreshToken);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}