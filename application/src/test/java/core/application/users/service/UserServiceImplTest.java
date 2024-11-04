package core.application.users.service;

import core.application.security.service.AuthenticatedUserService;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.SignupReqDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.dto.UserUpdateReqDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private UserServiceImpl userService;

    SignupReqDTO signupReqDTO;

    UserUpdateReqDTO userUpdateReqDTO;

    UserEntity userEntity;

    @BeforeEach
    public void init() {
        signupReqDTO = SignupReqDTO.builder()
                .userEmail("anyone@naver.com")
                .userPw("ilive123!123")
                .userName("anyany")
                .alias("anyone")
                .phoneNum("010-1111-1111")
                .role(UserRole.USER)
                .build();

        userUpdateReqDTO = UserUpdateReqDTO.builder()
                .userEmail("anyone@naver.com")
                .userPw("ilive123!123")
                .userName("anyany")
                .alias("anyone")
                .phoneNum("010-1111-1111")
                .build();

        userEntity = UserEntity.builder()
                .userEmail("anyone@naver.com")
                .userPw("ilive123!123")
                .userName("anyany")
                .alias("anyone")
                .phoneNum("010-1111-1111")
                .role(UserRole.USER)
                .build();
    }

    @Test
    @DisplayName("회원 가입 시 정상적으로 데이터 저장")
    public void userService_signup_returnMessageResponseDTO() {
        when(userRepository.saveNewUser(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        when(userRepository.findByUserEmail(signupReqDTO.getUserEmail())).thenReturn(Optional.of(userEntity));

        MessageResponseDTO messageResponseDTO = userService.signup(signupReqDTO);

        Assertions.assertThat(messageResponseDTO).isNotNull();
    }

    @Test
    public void userService_updateUser_returnMessageResponseDTO() {
        when(authenticatedUserService.getAuthenticatedUserEmail()).thenReturn(userUpdateReqDTO.getUserEmail());
        when(userRepository.findByUserEmail(signupReqDTO.getUserEmail())).thenReturn(Optional.of(userEntity));
        when(userRepository.editUserInfo(Mockito.any(UserEntity.class))).thenReturn(1);

        MessageResponseDTO messageResponseDTO = userService.updateUserInfo(userUpdateReqDTO);

        Assertions.assertThat(messageResponseDTO).isNotNull();
    }

    @Test
    public void userService_deleteUser_returnMessageResponseDTO() {
        when(authenticatedUserService.getAuthenticatedUserId()).thenReturn(userEntity.getUserId());
        when(userRepository.deleteUser(userEntity.getUserId())).thenReturn(1);

        MessageResponseDTO messageResponseDTO = userService.deleteUser();

        Assertions.assertThat(messageResponseDTO).isNotNull();
    }

    @Test
    public void userService_getUserByUserId_returnUserEntity() {
        when(userRepository.findByUserId(userEntity.getUserId())).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> userEntityReturn = userService.getUserByUserId(userEntity.getUserId());

        Assertions.assertThat(userEntityReturn).isPresent();
    }

    @Test
    public void userService_getUserByUserEmail_returnUserEntity() {
        when(userRepository.findByUserEmail(userEntity.getUserEmail())).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> userEntityReturn = userService.getUserByUserEmail(userEntity.getUserEmail());

        Assertions.assertThat(userEntityReturn).isPresent();
    }
}