package core.application.users.controller;

import core.application.users.models.dto.LoginRequestDTO;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.service.TokenService;
import core.application.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

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
    public MessageResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO);
    }

    /**
     * 사용자 회원가입
     * /users/signup
     */

    @PostMapping("/signup")
    public MessageResponseDTO singUp(@RequestBody UserDTO userDTO) {
        return userService.signup(userDTO);
    }

    /**
     * 사용자 로그아웃
     * /users/signout
     */

    @DeleteMapping("/signout") // access token과 refresh token 바로 중지
    public MessageResponseDTO logout(HttpServletRequest request) {
        return userService.logout();
    }

    /**
     * 사용자 정보 변경
     * /users/update
     */

    @PatchMapping("/update")
    public MessageResponseDTO updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    /**
     * 사용자 삭제
     * /users/delete
     */

    @DeleteMapping("/delete")
    public MessageResponseDTO deleteUser(HttpServletRequest request) {
        return userService.deleteUser();
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
