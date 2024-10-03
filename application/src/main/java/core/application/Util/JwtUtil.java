package core.application.Util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private SecretKey secretKey;
    private Long accessTimeout;
    private Long refreshTimeout;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret, @Value("${token.access.timeout}") Long accessTimeout, @Value("${token.refresh.timeout}") Long refreshTimeout) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTimeout = accessTimeout;
        this.refreshTimeout = refreshTimeout;
    }

    public String getUserEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userEmail", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String creatAccessToken(String userEmail, UUID userId, String role, String category) {
        return Jwts.builder()
                .claim("userEmail", userEmail)
                .claim("userId", userId)
                .claim("role", role)
                .claim("category", category)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTimeout))
                .signWith(secretKey)
                .compact();
    }

    public String creatRefreshToken(String userEmail, String category) {
        return Jwts.builder()
                .claim("userEmail", userEmail)
                .claim("category", category)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTimeout * 24 * 60 * 60 * 1000L))
                .signWith(secretKey)
                .compact();
    }
}
