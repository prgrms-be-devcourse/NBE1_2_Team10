package core.application.users.service;

import core.application.security.service.AuthenticatedUserService;
import core.application.users.exception.UserNotFoundException;
import core.application.users.models.dto.MessageResponseDTO;
import core.application.users.models.dto.SignupReqDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.dto.UserUpdateReqDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private UserServiceImpl userService;  // 테스트하려는 클래스

    private UUID mockUserId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUserId = UUID.randomUUID();  // 테스트용 mock UUID 생성
    }

    @Test
    @DisplayName("회원 가입 시 정상적으로 데이터 저장")
    void signup() {
        // given
        // 사용자 객체 생성
        SignupReqDTO newUser = SignupReqDTO.builder()
                .userEmail("winter@naver.com")
                .userPw("ilive123!")
                .userName("winter")
                .phoneNum("010-1111-1111")
                .role(UserRole.valueOf("USER"))
                .alias("winter")
                .build();

        newUser.encodePassword(passwordEncoder);

        // when
        userRepository.saveNewUser(newUser.toEntity());

        // then
        Optional<UserEntity> savedUser = userRepository.findByUserEmail("winter@naver.com");
        if (savedUser.isEmpty()) {
            throw new UserNotFoundException("회원 가입을 실패했습니다.");
        }
        System.out.println(savedUser.get().getUserId());

        assertNotNull(savedUser.get().getUserId());
        assertEquals(savedUser.get().getUserEmail(), newUser.getUserEmail());
    }

    @Test
    @DisplayName("회원 가입 시 데이터 중복으로 exception 발생")
    void signupWithExistingEmail_shouldThrowDuplicateException() {
        // given
        // 사용자 객체 생성
        SignupReqDTO newUser = SignupReqDTO.builder()
                .userEmail("karina@naver.com")
                .userPw("ilive123!")
                .userName("karina")
                .phoneNum("010-1111-1111")
                .role(UserRole.valueOf("USER"))
                .alias("karina")
                .build();

        newUser.encodePassword(passwordEncoder);

        // 사용자 객체 최초로 저장
        userRepository.saveNewUser(newUser.toEntity());

        // when & then
        assertThrows(DuplicateKeyException.class, () -> {
            // 중복된 유저를 다시 저장하려고 시도 (두 번째 저장)
            userRepository.saveNewUser(newUser.toEntity());
        });
    }

    @Test
    void updateUser() {
        // given
        // 회원 가입
        signup();

        // 사용자 객체 생성
        UserUpdateReqDTO updateReqUser = UserUpdateReqDTO.builder()
                .userEmail("winter@naver.com")
                .userPw("ilive123!123")
                .userName("winter")
                .alias("winter")
                .phoneNum("010-1111-1111")
                .build();

        String userEmail = updateReqUser.getUserEmail();

        // 기존 객체
        Optional<UserEntity> originUserEntity = userRepository.findByUserEmail(userEmail);
        if (originUserEntity.isEmpty()) {
            throw new UserNotFoundException("기존에 입력된 회원 정보가 존재하지 않습니다.");
        }

        // 정보 갱신 반영을 위한 객체 생성
        UserDTO updatedUser = UserDTO.builder()
                .userId(originUserEntity.get().getUserId()) // 기존 userId 유지
                .userPw(updateReqUser.getUserPw() != null ? updateReqUser.getUserPw() : originUserEntity.get().getUserPw())
                .alias(updateReqUser.getAlias() != null ? updateReqUser.getAlias() : originUserEntity.get().getAlias())
                .phoneNum(updateReqUser.getPhoneNum() != null ? updateReqUser.getPhoneNum() : originUserEntity.get().getPhoneNum())
                .userName(updateReqUser.getUserName() != null ? updateReqUser.getUserName() : originUserEntity.get().getUserName())
                .build();

        updatedUser.encodePassword(passwordEncoder);

        // when
        userRepository.editUserInfo(updatedUser.toEntity());

        // then
        Optional<UserEntity> savedUser = userRepository.findByUserEmail("winter@naver.com");
        if (savedUser.isEmpty()) {
            throw new UserNotFoundException("회원 가입을 실패했습니다.");
        }
        assertEquals(savedUser.get().getUserPw(), updatedUser.toEntity().getUserPw());
    }

    @Test
    void deleteUser() { // spring security와 관련해서 test 코드를 작성해야 하므로 추후 작성
    }

    @Test
    void getUserByUserId() {
        // given
        // 사용자 객체 생성
        SignupReqDTO newUser = SignupReqDTO.builder()
                .userEmail("ningning@naver.com")
                .userPw("ilive123!")
                .userName("ningning")
                .phoneNum("010-1111-1111")
                .role(UserRole.valueOf("USER"))
                .alias("ningning")
                .build();

        newUser.encodePassword(passwordEncoder);

        userRepository.saveNewUser(newUser.toEntity());

        UUID userId = userRepository.findByUserEmail("ningning@naver.com").get().getUserId();

        // when
        Optional<UserEntity> savedUser = userRepository.findByUserId(userId);

        // then
        assertEquals(savedUser.get().getUserPw(), newUser.toEntity().getUserPw());
    }

    @Test
    void getUserByUserEmail() {
        // given
        // 사용자 객체 생성
        SignupReqDTO newUser = SignupReqDTO.builder()
                .userEmail("giselle@naver.com")
                .userPw("ilive123!")
                .userName("giselle")
                .phoneNum("010-1111-1111")
                .role(UserRole.valueOf("USER"))
                .alias("giselle")
                .build();

        newUser.encodePassword(passwordEncoder);

        userRepository.saveNewUser(newUser.toEntity());

        // when
        Optional<UserEntity> savedUser = userRepository.findByUserEmail("giselle@naver.com");

        // then
        assertEquals(savedUser.get().getUserPw(), newUser.toEntity().getUserPw());
    }
}