package core.application.security;

import core.application.users.models.entities.UserEntity;
import core.application.users.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * JWT 토큰을 관리하는 서비스 클래스
 *
 * 이 클래스는 액세스 토큰과 리프레시 토큰을 검증하고, 사용자 정보를 추출하며,
 * 새로운 액세스 토큰을 재발급하는 기능 제공
 */
@Service
public class TokenService {
    private JwtTokenUtil jwtUtil;
    private UserService userService;
    private RedisService redisService;

    /**
     * TokenService의 생성자
     *
     * @param jwtUtil JWT 유틸리티 클래스
     * @param userService 사용자 관련 서비스
     * @param redisService Redis 관련 서비스
     */
    TokenService(JwtTokenUtil jwtUtil, UserService userService, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.redisService = redisService;
    }

    /**
     * HTTP 요청에서 리프레시 토큰을 가져옴
     *
     * @param request HTTP 요청 객체
     * @return 리프레시 토큰 문자열
     */
    public String getRefreshToken(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }
        return refreshToken;
    }

    /**
     * 주어진 리프레시 토큰의 유효성을 검증함
     *
     * @param refreshToken 리프레시 토큰 문자열
     * @return 유효성 검사 결과 메시지
     */
    public String validateRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            //response status code
            return "refresh token null";
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return "refresh token expired";
        }

        String category = jwtUtil.getCategory(refreshToken);

        if(!category.equals("refresh")) {
            return "refresh token expired";
        }

        if (redisService.getValue(jwtUtil.getUserEmail(refreshToken)) == null) {
            return "refresh token not recognized.";
        }

        return "valid token";
    }

    /**
     * 주어진 액세스 토큰의 유효성 검증
     *
     * @param accessToken 액세스 토큰 문자열
     * @return 유효성 검사 결과 메시지
     */
    public String validateAccessToken(String accessToken) {
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            return "access token expired";
        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            return "Invalid access token";
        }
        return "valid token";
    }

    /**
     * 주어진 액세스 토큰으로부터 사용자 정보 추출
     *
     * @param accessToken 액세스 토큰 문자열
     * @return {@link Optional}{@code <}{@link UserEntity}{@code >}
     *         사용자 정보가 포함된 Optional 객체
     */
    public Optional<UserEntity> getUserByAccessToken(String accessToken) {

        if (accessToken == null) {
            return Optional.empty();
        }

        String userEmail = jwtUtil.getUserEmail(accessToken);
        Optional<UserEntity> userEntity = userService.getUserByUserEmail(userEmail);

        if (userEntity.isEmpty()) {
            return Optional.empty();
        }

        // 주요한 정보 제외한 UserEntity 반환
        Optional<UserEntity> userWithoutSensitiveInfo = Optional.ofNullable(UserEntity.builder()
                .userEmail(userEntity.get().getUserEmail())
                .userId(userEntity.get().getUserId())
                .userName(userEntity.get().getUserName())
                .role(userEntity.get().getRole())
                .build());

        return userWithoutSensitiveInfo;
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 재발급
     *
     * @param request HTTP 요청 객체
     * @return 새로 발급된 액세스 토큰 문자열
     */
    public String reissueAccessToken(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);

        if (validateRefreshToken(refreshToken).equals("valid token")) {
            String userEmail =  jwtUtil.getUserEmail(refreshToken);
            Optional<UserEntity> userEntity = userService.getUserByUserEmail(userEmail);
            UUID userId = userEntity.get().getUserId();
            String role = userEntity.get().getRole().toString();

            return jwtUtil.creatAccessToken(userEmail, userId, role, "access");
        } else {
            return null;
        }
    }

    /**
     * 주어진 리프레시 토큰 비활성화
     *
     * @param refreshToken 리프레시 토큰 문자열
     * @return 비활성화 결과 메시지
     */
    public String inactiveRefreshToken(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);
        if (validateRefreshToken(refreshToken).equals("valid token")) {
            redisService.deleteValue(jwtUtil.getUserEmail(refreshToken));
        }
        return "inactive refresh token success";
    }
}
