package core.application.users.service;

import core.application.security.AuthenticatedUserService;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticatedUserService authenticatedUserInfo;


    @Test
    @DisplayName("회원 가입 시 정상적으로 데이터 저장")
    void signup() {
        // given
        // 사용자 객체 생성
        UserDTO newUser = UserDTO.builder()
                .userEmail("winter@naver.com")
                .userPw("ilive123!")
                .userName("winter")
                .phoneNum("010-1111-1111")
                .role(UserRole.valueOf("USER"))
                .alias("winter")
                .build();

        // when
        userRepository.saveNewUser(newUser.toEntity());

        // then
        UserEntity savedUser = userRepository.findByUserEmail("winter@naver.com").get();
        assertEquals(savedUser.getUserEmail(), newUser.getUserEmail());
    }

    @Test
    @DisplayName("회원 가입 시 데이터 중복으로 exception 발생")
    void signup_DuplicateKeyException() {
        // given
        // 사용자 객체 생성
        UserDTO newUser = UserDTO.builder()
                .userEmail("karina@naver.com")
                .userPw("ilive123!")
                .userName("karina")
                .phoneNum("010-1111-1111")
                .role(UserRole.valueOf("USER"))
                .alias("karina")
                .build();

        // 사용자 객체 최초로 저장
        userRepository.saveNewUser(newUser.toEntity());

        // when & then
        assertThrows(DuplicateKeyException.class, () -> {
            // 중복된 유저를 다시 저장하려고 시도 (두 번째 저장)
            userRepository.saveNewUser(newUser.toEntity());
        });
    }

//    @Test
//    @DisplayName("회원 가입 시 데이터에 공백 포함 시 에러 발생")
//    void signup_includeBlank() {
//        // given
//        // 공백을 포함한 사용자 객체 생성
//        UserDTO newUser = UserDTO.builder()
//                .userEmail("karw we a@naver.com")
//                .userPw("ili ve123!")
//                .userName("kar ina")
//                .phoneNum("010-111 1-1111")
//                .role(UserRole.valueOf("USER"))
//                .alias("kari na")
//                .build();
//
//        // when & then
//        assertThrows(AssertionFailedError.class, () -> {
//            // 중복된 유저를 다시 저장하려고 시도 (두 번째 저장)
//            userRepository.saveNewUser(newUser.toEntity());
//        });
//    }

    @Test
    void updateUserInfo() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUserByUserId() {
    }

    @Test
    void getUserByUserEmail() {
    }
}