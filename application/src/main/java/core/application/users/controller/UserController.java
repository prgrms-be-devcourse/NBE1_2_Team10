package core.application.users.controller;

import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.security.TokenService;
import core.application.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 요청을 처리하는 컨트롤러
 * 사용자 로그인, 회원가입, 로그아웃, 정보 변경 및 삭제 등의 요청을 처리
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    /**
     * UserController 생성자.
     *
     * @param userService 사용자 서비스
     * @param tokenService 토큰 서비스
     */
    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    /**
     * 사용자 로그인
     * /users/signin
     */

    @PostMapping("/signin")
    public void login() {
    }

    /**
     * 사용자 회원가입
     * /users/signup
     */

    @PostMapping("/signup")
    public MessageResponseDTO singUp(@Valid @RequestBody UserDTO userDTO) {
        return userService.signup(userDTO);
    }

    /**
     * 사용자 로그아웃
     * /users/signout
     */

    @DeleteMapping("/signout")
    public void logout() {
    }

    /**
     * 사용자 정보 변경
     * /users/update
     */

    @PatchMapping("/update")
    public MessageResponseDTO updateUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    /**
     * 사용자 삭제
     * /users/delete
     */

    @DeleteMapping("/delete")
    public MessageResponseDTO deleteUser(HttpServletRequest request) {
        MessageResponseDTO messageResponseDTO = userService.deleteUser();
        if (messageResponseDTO != null) {
            tokenService.inactiveRefreshToken(request);
        }
        return messageResponseDTO;
    }

    /**
     * access token 재발급
     * /users/reissue
     */

    @GetMapping("/reissue") // 추후 반환 값에 수정
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String reissuedAccessToken = tokenService.reissueAccessToken(request);

        if (reissuedAccessToken != null) {
            response.setHeader("accessToken", reissuedAccessToken);
        }
    }
}
