package core.application.users.service;

import core.application.security.AuthenticatedUserService;
import core.application.users.exception.DuplicateEmailException;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.dto.UserRequestDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.repositories.UserRepository;
import core.application.users.repositories.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.DuplicateFormatFlagsException;
import java.util.Optional;
import java.util.UUID;

/**
 * 사용자 관련 서비스 구현 클래스
 * 이 클래스는 사용자 회원가입, 정보 수정, 삭제 및 조회 기능을 제공
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserInfo;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 생성자
     *
     * @param userRepositoryImpl 사용자 리포지토리 구현체
     * @param authenticatedUserInfo 인증된 사용자 서비스
     */
    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl, AuthenticatedUserService authenticatedUserInfo,
		BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepositoryImpl;
        this.authenticatedUserInfo = authenticatedUserInfo;
		this.passwordEncoder = passwordEncoder;
	}

    /**
     * 사용자 회원가입 처리
     *
     * @param userRequestDTO 사용자 정보를 담고 있는 DTO
     * @return 회원가입 결과 메시지를 포함하는 MessageResponseDTO
     */
    @Override
    public MessageResponseDTO signup(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getUserEmail())) {
            throw new DuplicateEmailException("중복된 이메일입니다.");
        }
        userDTO.encodePassword(passwordEncoder);
        if (userDTO.getAlias() == null) {
            String email = userDTO.getUserEmail();
          
            UserDTO userWithAlias = UserDTO.builder()
                    .userEmail(userRequestDTO.getUserEmail())
                    .userPw(userRequestDTO.getUserPw())
                    .userName(userRequestDTO.getUserName())
                    .alias(email.substring(0, email.indexOf("@")))
                    .phoneNum(userRequestDTO.getPhoneNum())
                    .role(userRequestDTO.getRole())
                    .build();
            userRepository.saveNewUser(userWithAlias.toEntity());
        } else {
            userRepository.saveNewUser(userRequestDTO.toEntity());
        }
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(userRequestDTO.getUserEmail());
        return new MessageResponseDTO(userEntity.get().getUserId(), "signUp success");
    }

    /**
     * 사용자 정보 수정
     *
     * @param userRequestDTO 수정할 사용자 정보를 담고 있는 DTO
     * @return 수정 결과 메시지를 포함하는 MessageResponseDTO, 수정이 실패할 경우 null
     */
    @Override
    public MessageResponseDTO updateUserInfo(UserRequestDTO userRequestDTO) {
        String userEmail = authenticatedUserInfo.getAuthenticatedUserEmail();

        // 요청 시 토큰의 userEmail과 다른 userEmail을 가지고 있는 사용자의 정보를 바꾸려고 할 때 반환 값 null
        if (!userEmail.equals(userRequestDTO.getUserEmail())) {
            return null;
        }

        UserEntity originUserEntity = userRepository.findByUserEmail(userRequestDTO.getUserEmail()).get();

        // 새로운 UserEntity를 기존 값과 DTO 값을 비교하여 생성
        UserEntity updatedUserEntity = UserEntity.builder()
                .userId(originUserEntity.getUserId()) // 기존 userId 유지
                .userPw(userRequestDTO.getUserPw() != null ? userRequestDTO.getUserPw() : originUserEntity.getUserPw()) // userPw 업데이트
                .role(userRequestDTO.getRole() != null ? userRequestDTO.getRole() : originUserEntity.getRole()) // role 업데이트
                .alias(userRequestDTO.getAlias() != null ? userRequestDTO.getAlias() : originUserEntity.getAlias()) // alias 업데이트
                .phoneNum(userRequestDTO.getPhoneNum() != null ? userRequestDTO.getPhoneNum() : originUserEntity.getPhoneNum()) // phoneNum 업데이트
                .userName(userRequestDTO.getUserName() != null ? userRequestDTO.getUserName() : originUserEntity.getUserName()) // userName 업데이트
                .build();

        if (userRepository.editUserInfo(userRequestDTO.toEntity()) == 1) {
            return new MessageResponseDTO(originUserEntity.getUserId(), "update success");
        }
        return null;
    }

    /**
     * 현재 인증된 사용자 계정 삭제
     *
     * @return 삭제 결과 메시지를 포함하는 MessageResponseDTO, 삭제가 실패할 경우 null
     */
    @Override
    public MessageResponseDTO deleteUser() {
        UUID userId = authenticatedUserInfo.getAuthenticatedUserId();
        if (userRepository.deleteUser(userId) == 1) {
            return new MessageResponseDTO(userId, "delete success");
        }
        return null;
    }

    /**
     * 사용자 ID로 사용자 정보를 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 사용자 엔티티를 포함하는 Optional, 사용자 정보를 찾지 못한 경우 빈 Optional 반환
     */
    public Optional<UserEntity> getUserByUserId(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * 사용자 이메일로 사용자 정보를 조회
     *
     * @param userEmail 조회할 사용자 이메일
     * @return 사용자 엔티티를 포함하는 Optional, 사용자 정보를 찾지 못한 경우 빈 Optional 반환
     */
    public Optional<UserEntity> getUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }
}
