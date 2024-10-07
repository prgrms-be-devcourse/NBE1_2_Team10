package core.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 인증된 사용자 정보를 제공하는 서비스 클래스
 *
 * 이 클래스는 현재 인증된 사용자의 ID, 이메일, 역할을 가져오는 메서드를 제공
 * Spring Security의 SecurityContext에서 인증 정보를 읽어옴
 */
@Service
public class AuthenticatedUserService {
    /**
     * 현재 인증된 사용자의 ID를 반환
     *
     * @return 인증된 사용자의 UUID, 인증되지 않은 경우 null 반환
     */
    public UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return customUserDetails.getUserId();  // UserId 반환
        }
        return null; // 인증되지 않은 사용자일 경우
    }

    /**
     * 현재 인증된 사용자의 이메일을 반환
     *
     * @return 인증된 사용자의 이메일, 인증되지 않은 경우 null 반환
     */
    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return customUserDetails.getUserEmail();  // UserEmail 반환
        }
        return null; // 인증되지 않은 사용자일 경우
    }

    /**
     * 현재 인증된 사용자의 역할을 반환
     *
     * @return 인증된 사용자의 역할 문자열, 인증되지 않은 경우 null 반환
     */
    public String getAuthenticatedRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return customUserDetails.getUserRole().toString();  // UserRole 반환
        }
        return null; // 인증되지 않은 사용자일 경우
    }
}
