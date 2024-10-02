package core.application.filter;

import core.application.Util.JwtUtil;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class JWTFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JWTFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // 여기서 refresh token 확인하고 검증이 안된다면 redirect
            filterChain.doFilter(request, response);

            throw new AuthenticationException("Invalid JWT"){};
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String userEmail = jwtUtil.getUserEmail(token);
        UUID userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        //userEntity를 생성하여 값 set
        Optional<UserEntity> userEntity = Optional.ofNullable(UserEntity.builder()
                .userEmail(userEmail)
                .userId(userId)
                .role(UserRole.valueOf(role))
                .build());

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
