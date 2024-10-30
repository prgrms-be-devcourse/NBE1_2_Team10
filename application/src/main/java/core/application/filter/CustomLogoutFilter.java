package core.application.filter;

import core.application.api.response.ApiResponse;
import core.application.api.response.code.Message;
import core.application.security.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 사용자 로그아웃 요청을 처리하는 커스텀 필터
 * Spring의 GenericFilterBean을 확장하여 로그아웃 시 Refresh 토큰 처리
 */
public class CustomLogoutFilter extends GenericFilterBean {
    private final TokenService tokenService;

    /**
     * 생성자.
     *
     * @param tokenService 토큰 관리 서비스
     */
    public CustomLogoutFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 필터 체인에서 요청 필터링
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param chain 필터 체인
     * @throws IOException 입출력 오류 발생 시
     * @throws ServletException 서블릿 관련 오류 발생 시
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    /**
     * HTTP 요청을 필터링하고 로그아웃 로직 처리
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws IOException 입출력 오류 발생 시
     * @throws ServletException 서블릿 관련 오류 발생 시
     */
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 요청 URI 및 메소드 확인
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestUri.matches("^/users/signout$") || !requestMethod.equals("DELETE")) {

            // 로그아웃 경로나 DELETE 메소드가 아닌 경우 필터 체인 진행
            filterChain.doFilter(request, response);
            return;
        }

        // Refresh 토큰 가져옴
        String refreshToken = tokenService.getRefreshToken(request);

        // Refresh 토큰 유효성 검증
        if (tokenService.isRefreshTokenValid(refreshToken)) {
            // 로그아웃 진행 -> Refresh 토큰 DB에서 제거
            tokenService.inactiveRefreshToken(request);
        }

        // Refresh 토큰 쿠키 값을 0으로 설정하여 삭제
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie); // 쿠키 추가
        ApiResponse.onDeleteSuccess(Message.createMessage("Refresh Token 삭제를 완료했습니다."));
    }
}
