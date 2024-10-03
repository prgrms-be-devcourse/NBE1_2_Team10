package core.application.users.service;

import core.application.Util.JwtUtil;
import core.application.users.models.entities.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    private JwtUtil jwtUtil;
    private UserService userService;
    private RedisService redisService;

    TokenService(JwtUtil jwtUtil, UserService userService, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.redisService = redisService;
    }

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

    public String validateRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            //response status code
            return "refresh token null";
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            //response status code
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

    public String validateAccessToken(String accessToken) {

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            return "access token expired";
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            return "Invalid access token";
        }
        return "valid token";
    }

    public Optional<UserEntity> getUserByAccessToken(String accessToken) {

        if (accessToken == null) {
            return Optional.empty();
        }

        System.out.println(accessToken);
//        UUID userId = jwtUtil.getUserId(accessToken);
//        System.out.println("user id " + userId);
//        Optional<UserEntity> userEntity = userService.getUserByUserId(userId);
        String userEmail = jwtUtil.getUserEmail(accessToken);
        System.out.println(userEmail);
        Optional<UserEntity> userEntity = userService.getUserByUserEmail(userEmail);

        if (userEntity.isEmpty()) {
            return Optional.empty();
        }

        //userEntity를 생성하여 값 set
        Optional<UserEntity> userWithoutSensitiveInfo = Optional.ofNullable(UserEntity.builder()
                .userEmail(userEntity.get().getUserEmail())
                .userId(userEntity.get().getUserId())
                .userName(userEntity.get().getUserName())
                .role(userEntity.get().getRole())
                .build());

        return userWithoutSensitiveInfo;
    }

    public String reissueAccessToken(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);

        if (validateRefreshToken(refreshToken).equals("valid token")) {
            String userEmail =  jwtUtil.getUserEmail(refreshToken);
            Optional<UserEntity> userEntity = userService.getUserByUserEmail(userEmail);
            UUID userId = userEntity.get().getUserId();
            String role = userEntity.get().getRole().toString();

            // return access token
            return jwtUtil.creatAccessToken(userEmail, userId, role, "access");
        } else {
            return null;
        }
    }

    public String inactiveAccessToken(String refreshToken) {
        System.out.println(refreshToken);
        if (validateRefreshToken(refreshToken).equals("valid token")) {
            redisService.deleteValue(jwtUtil.getUserEmail(refreshToken));
        }
        return "inactive refresh token success";
    }
}
