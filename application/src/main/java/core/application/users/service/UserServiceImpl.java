package core.application.users.service;

import core.application.users.models.dto.LoginRequestDTO;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.repositories.UserRepository;
import core.application.users.repositories.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl) {
        this.userRepository = userRepositoryImpl;
    }

    // 사용자 회원 가입
    @Override
    public MessageResponseDTO signup(UserDTO userDTO) {
        userRepository.saveNewUser(userDTO.toEntity());
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(userDTO.getUserEmail());
        return new MessageResponseDTO(userEntity.get().getUserId(), "signUp success");
    }

    // 사용자 로그인 // token 반환으로 추후 수정 필요
    @Override
    public MessageResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<UserEntity> user = userRepository.findByUserEmailAndPassword(loginRequestDTO.getUserEmail(), loginRequestDTO.getUserPw());
        MessageResponseDTO messageResponseDTO;
        if (user.isPresent()) {
            messageResponseDTO = new MessageResponseDTO(user.get().getUserId(), "login success");
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return messageResponseDTO;
    }

    // 사용자 로그아웃
    @Override
    public MessageResponseDTO logout(UserDTO userDTO) {
        // token을 expired 리스트 만들어서 거기에 넣어두기 아마도? access token으로 어떻게 하는지 예시들 찾아보기
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
    public MessageResponseDTO deleteUser(UUID userId) {
        userRepository.deleteUser(userId);
        return new MessageResponseDTO(userId, "user deleted successfully");
    }
}
