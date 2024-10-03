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

    // 사용자 정보 변경
    @Override
    public MessageResponseDTO updateUserInfo(UserDTO userDTO) {
        if (userRepository.editUserInfo(userDTO.toEntity()) == 1) {
            return new MessageResponseDTO(userDTO.getUserId(), "update success");
        }
        return null;
    }

    // 사용자 계정 삭제
    @Override
    public MessageResponseDTO deleteUser() {
        UUID userId = authenticatedUserInfo.getAuthenticatedUserId();
        if (userRepository.deleteUser(userId) == 1) {
            return new MessageResponseDTO(userId, "delete success");
        }
        return null;
    }

    // 사용자 계정 정보 조회
    public Optional<UserEntity> getUserByUserId(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    // 사용자 계정 정보 조회
    public Optional<UserEntity> getUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }
}
