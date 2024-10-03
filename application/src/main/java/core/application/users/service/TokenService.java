package core.application.users.service;

import core.application.Util.JwtUtil;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
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

        return "success";
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
        return "success";
    }

    public Optional<UserEntity> getUserByAccessToken(String accessToken) {
        if (!validateAccessToken(accessToken).equals("success")) {
            return Optional.empty();
        }

        //토큰에서 userEmail과 role 획득
        String userEmail = jwtUtil.getUserEmail(accessToken);
        UUID userId = jwtUtil.getUserId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        if (userService.getUserByUserId(userId).isEmpty()) {
            return Optional.empty();
        }

        //userEntity를 생성하여 값 set
        Optional<UserEntity> userEntity = Optional.ofNullable(UserEntity.builder()
                .userEmail(userEmail)
                .userId(userId)
                .role(UserRole.valueOf(role))
                .build());

        return userEntity;
    }

    public String reissueAccessToken(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);

        if (validateRefreshToken(refreshToken).equals("success")) {
            UUID userId = jwtUtil.getUserId(refreshToken);
            Optional<UserEntity> userEntity = userService.getUserByUserId(userId);
            String userEmail = userEntity.get().getUserEmail();
            String role = userEntity.get().getRole().toString();

            // return access token
            return jwtUtil.creatAccessToken(userEmail, userId, role, "access");
        } else {
            return null;
        }
    }

    public String inactiveAccessToken(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);
        System.out.println(refreshToken);
        if (validateRefreshToken(refreshToken).equals("success")) {
            redisService.deleteValue(jwtUtil.getUserEmail(refreshToken));
        }
        return "inactive refresh token success";
    }
}
