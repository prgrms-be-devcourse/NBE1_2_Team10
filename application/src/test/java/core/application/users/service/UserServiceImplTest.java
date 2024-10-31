package core.application.users.service;

import core.application.users.exception.UserNotFoundException;
import core.application.users.models.dto.SignupReqDTO;
import core.application.users.models.dto.UserDTO;
import core.application.users.models.dto.UserUpdateReqDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false) // 롤백하지 않도록 설정
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

        newUser.encodePassword();

        UserEntity userEntity = newUser.toEntity();

        // when
        System.out.println(userRepository.saveNewUser(newUser.toEntity()));;

        // then
        Optional<UserEntity> savedUser = userRepository.findByUserEmail("winter@naver.com");
        if (savedUser.isEmpty()) {
            throw new UserNotFoundException("회원 가입을 실패했습니다.");
        }
        System.out.println(savedUser.get().getUserId());

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

        newUser.encodePassword();

        // 사용자 객체 최초로 저장
        userRepository.saveNewUser(newUser.toEntity());

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> {
            // 중복된 유저를 다시 저장하려고 시도 (두 번째 저장)
            userRepository.saveNewUser(newUser.toEntity());
        });
    }

    @Test
    void updateUser() {
        // given
        // 회원 가입
        SignupReqDTO newUser = SignupReqDTO.builder()
                .userEmail("anyone@naver.com")
                .userPw("ilive123!")
                .userName("any")
                .phoneNum("010-1111-1111")
                .role(UserRole.valueOf("USER"))
                .alias("anyone")
                .build();

        userService.signup(newUser);

        // 사용자 객체 생성
        UserUpdateReqDTO updateReqUser = UserUpdateReqDTO.builder()
                .userEmail("anyone@naver.com")
                .userPw("ilive123!123")
                .userName("anyany")
                .alias("anyone")
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
                .userEmail(originUserEntity.get().getUserEmail())
                .userId(originUserEntity.get().getUserId()) // 기존 userId 유지
                .userPw(updateReqUser.getUserPw() != null ? updateReqUser.getUserPw() : originUserEntity.get().getUserPw())
                .alias(updateReqUser.getAlias() != null ? updateReqUser.getAlias() : originUserEntity.get().getAlias())
                .role(originUserEntity.get().getRole())
                .phoneNum(updateReqUser.getPhoneNum() != null ? updateReqUser.getPhoneNum() : originUserEntity.get().getPhoneNum())
                .userName(updateReqUser.getUserName() != null ? updateReqUser.getUserName() : originUserEntity.get().getUserName())
                .build();

        updatedUser.encodePassword();

        // when
        userRepository.editUserInfo(updatedUser.toEntity());

        // then
        Optional<UserEntity> savedUser = userRepository.findByUserEmail(originUserEntity.get().getUserEmail());
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

        newUser.encodePassword();

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

        newUser.encodePassword();

        userRepository.saveNewUser(newUser.toEntity());

        // when
        Optional<UserEntity> savedUser = userRepository.findByUserEmail("giselle@naver.com");

        // then
        assertEquals(savedUser.get().getUserPw(), newUser.toEntity().getUserPw());
    }
}