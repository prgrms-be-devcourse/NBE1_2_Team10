package core.application.filter;

import core.application.users.models.entities.UserEntity;
import core.application.security.CustomUserDetails;
import core.application.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT 기반의 인증을 처리하는 필터
 *
 * HTTP 요청에서 Access Token을 추출하여
 * 사용자의 인증 정보를 SecurityContext에 설정
 */
public class JWTFilter extends OncePerRequestFilter {
    TokenService tokenService;

    /**
     * JWTFilter 생성자
     *
     * @param tokenService JWT와 관련된 사용자 정보를 처리하는 서비스
     */
    public JWTFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 요청을 필터링하여 Access Token을 검사하고,
     * 유효한 경우 사용자의 인증 정보를 SecurityContext에 설정함
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 다음 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("accessToken");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            System.out.println("Access token is null");
            filterChain.doFilter(request, response);
            return;
        }

        Optional<UserEntity> userEntity = tokenService.getUserByAccessToken(accessToken);

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
