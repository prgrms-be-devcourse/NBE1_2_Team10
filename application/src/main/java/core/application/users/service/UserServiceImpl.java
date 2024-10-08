package core.application.users.service;

import core.application.security.AuthenticatedUserService;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.repositories.UserRepository;
import core.application.users.repositories.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 생성자
     *
     * @param userRepositoryImpl 사용자 리포지토리 구현체
     * @param authenticatedUserInfo 인증된 사용자 서비스
     */
    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl, AuthenticatedUserService authenticatedUserInfo) {
        this.userRepository = userRepositoryImpl;
        this.authenticatedUserInfo = authenticatedUserInfo;
    }

    /**
     * 사용자 회원가입 처리
     *
     * @param userDTO 사용자 정보를 담고 있는 DTO
     * @return 회원가입 결과 메시지를 포함하는 MessageResponseDTO
     */
    @Override
    public MessageResponseDTO signup(UserDTO userDTO) {
        if (userDTO.getAlias() == null) {
            String email = userDTO.getUserEmail();
            UserDTO userWithAlias = UserDTO.builder()
                    .userEmail(userDTO.getUserEmail())
                    .userPw(userDTO.getUserPw())
                    .userName(userDTO.getUserName())
                    .alias(email.substring(0, email.indexOf("@")))
                    .phoneNum(userDTO.getPhoneNum())
                    .role(userDTO.getRole())
                    .build();
            userRepository.saveNewUser(userWithAlias.toEntity());
        } else {
            userRepository.saveNewUser(userDTO.toEntity());
        }
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(userDTO.getUserEmail());
        return new MessageResponseDTO(userEntity.get().getUserId(), "signUp success");
    }

    /**
     * 사용자 정보 수정
     *
     * @param userDTO 수정할 사용자 정보를 담고 있는 DTO
     * @return 수정 결과 메시지를 포함하는 MessageResponseDTO, 수정이 실패할 경우 null
     */
    @Override
    public MessageResponseDTO updateUserInfo(UserDTO userDTO) {
        UserEntity originUserEntity = userRepository.findByUserId(userDTO.getUserId()).get();

        // 새로운 UserEntity를 기존 값과 DTO 값을 비교하여 생성
        UserEntity updatedUserEntity = UserEntity.builder()
                .userId(originUserEntity.getUserId()) // 기존 userId 유지
                .userPw(userDTO.getUserPw() != null ? userDTO.getUserPw() : originUserEntity.getUserPw()) // userPw 업데이트
                .role(userDTO.getRole() != null ? userDTO.getRole() : originUserEntity.getRole()) // role 업데이트
                .alias(userDTO.getAlias() != null ? userDTO.getAlias() : originUserEntity.getAlias()) // alias 업데이트
                .phoneNum(userDTO.getPhoneNum() != null ? userDTO.getPhoneNum() : originUserEntity.getPhoneNum()) // phoneNum 업데이트
                .userName(userDTO.getUserName() != null ? userDTO.getUserName() : originUserEntity.getUserName()) // userName 업데이트
                .build();

        if (userRepository.editUserInfo(userDTO.toEntity()) == 1) {
            return new MessageResponseDTO(userDTO.getUserId(), "update success");
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
