package core.application.security;

import core.application.security.service.AuthenticatedUserService;
import core.application.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

// 추후 삭제
@Controller
public class HomeTestController {
    private final AuthenticatedUserService authenticatedUserService;
    private final TokenService tokenService;

    public HomeTestController(AuthenticatedUserService authenticatedUserService, TokenService tokenService) {
        this.authenticatedUserService = authenticatedUserService;
        this.tokenService = tokenService;
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/invalid")
    public String logout(HttpSession session) {
        session.invalidate();  // 현재 세션 무효화
        return "redirect:/home"; // 홈 페이지로 리디렉션
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        String token = tokenService.getOAuthAccessToken(request);
        System.out.println("profile : " + token);
        System.out.println(tokenService.getUserByAccessToken(token));

        UUID userId = authenticatedUserService.getAuthenticatedUserId();
        String role = authenticatedUserService.getAuthenticatedRole();
        String email = authenticatedUserService.getAuthenticatedUserEmail();
        if (email != null) {
            model.addAttribute("email", email);
            model.addAttribute("userId", userId);
            model.addAttribute("role", role);
        }
        return "profile";
    }
}
