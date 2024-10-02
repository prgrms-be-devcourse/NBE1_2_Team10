package core.application.users.controller;

import core.application.Util.JwtUtil;
import core.application.users.models.dto.LoginRequestDTO;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 사용자 로그인
     * /users/signin
     */

    @PostMapping("/signin")
    public MessageResponseDTO login (@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO);
    }

    /**
     * 사용자 회원가입
     * /users/signup
     */

    @PostMapping("/signup")
        public MessageResponseDTO singUp (@RequestBody UserDTO userDTO) {
        return userService.signup(userDTO);
    }

    /**
     * 사용자 로그아웃
     * /users/signout
     */

//    @PostMapping("/signout") // access token과 refresh token 바로 중지
//    public MessageResponseDTO logout (token? email? userDTO?) {
//        return null;
//    }

    /**
     * 사용자 정보 변경
     * /users/update
     */

    @PatchMapping("/update")
    public MessageResponseDTO updateUser (@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        return userService.updateUserInfo(userDTO);
    }

    /**
     * 사용자 삭제
     * /users/delete
     */

    @DeleteMapping("/delete") // access token과 refresh token 바로 중지
    public MessageResponseDTO deleteUser (HttpServletRequest request) {
        UUID userId = jwtUtil.getUserId(request.getHeader("Authorization").substring(7));
        return userService.deleteUser(userId);
    }

//    @PostMapping("/token") // filter 단에서 처리하는 것이 필요할 것 같음 -> access token 재발급이 필요하다는 뜻은 refresh token은 살아있다는 뜻이므로
}
