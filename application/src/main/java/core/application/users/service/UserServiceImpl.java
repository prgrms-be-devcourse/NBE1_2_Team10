package core.application.users.service;

import core.application.users.models.dto.LoginRequestDTO;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.repositories.UserRepository;
import core.application.users.repositories.UserRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticatedUserInfo authenticatedUserInfo;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl, AuthenticatedUserInfo authenticatedUserInfo) {
        this.userRepository = userRepositoryImpl;
        this.authenticatedUserInfo = authenticatedUserInfo;
    }

    // 사용자 회원 가입
    @Override
    public MessageResponseDTO signup(UserDTO userDTO) {
        userRepository.saveNewUser(userDTO.toEntity());
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(userDTO.getUserEmail());
        return new MessageResponseDTO(userEntity.get().getUserId(), "signUp success");
    }

    // 사용자 로그인 // token 반환으로 추후 수정 필요? header랑 이런데 들어가는데?? 일단 고민좀
    @Override
    public MessageResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<UserEntity> user = userRepository.findByUserEmailAndPassword(loginRequestDTO.getUserEmail(), loginRequestDTO.getUserPw());
        MessageResponseDTO messageResponseDTO;
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        messageResponseDTO = new MessageResponseDTO(user.get().getUserId(), "login success");
        return messageResponseDTO;
    }

    // 사용자 로그아웃 // 추후 구현
    @Override
    public MessageResponseDTO logout() {
        // token을 expired 리스트 만들어서 거기에 넣어두기 아마도? access token으로 어떻게 하는지 예시들 찾아보기
        UUID userId = authenticatedUserInfo.getAuthenticatedUserId();
        return null;
    }

    // 사용자 정보 변경
    @Override
    public MessageResponseDTO updateUserInfo(UserDTO userDTO) {
        userRepository.editUserInfo(userDTO.toEntity());
        return new MessageResponseDTO(userDTO.getUserId(), "user updated successfully");
    }

    // 사용자 계정 삭제
    @Override
    public MessageResponseDTO deleteUser() {
        UUID userId = authenticatedUserInfo.getAuthenticatedUserId();
        userRepository.deleteUser(userId);
        return new MessageResponseDTO(userId, "user deleted successfully");
    }

    // 사용자 계정 정보 조회
    public Optional<UserEntity> getUserByUserId(UUID userId) {
        return userRepository.findByUserId(userId);
    }
}
