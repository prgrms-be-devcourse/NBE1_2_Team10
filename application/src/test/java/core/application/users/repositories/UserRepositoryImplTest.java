package core.application.users.repositories;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;

@SpringBootTest
@Transactional
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepo;

    private static UserEntity testUser; // USER
    private static UserEntity testUser2; // ADMIN


    @BeforeEach
    void init() {

        testUser = UserEntity.builder()
                .userEmail("test@test.com")
                .userPw("test")
                .role(UserRole.USER)
                .alias("소은")
                .phoneNum("010-0000-0000")
                .userName("정소은")
                .build();

        testUser2  = UserEntity.builder()
                .userEmail("test2@test.com")
                .userPw("test")
                .role(UserRole.ADMIN)
                .alias("소은")
                .phoneNum("010-0000-0000")
                .userName("정소은")
                .build();

    }

    @Test
    @Transactional
    @DisplayName("회원가입 / 새로운 유저 저장")
    void saveNewUser() {
        // Given

        // When
        userRepo.saveNewUser(testUser);
        UserEntity user = userRepo.findByUserEmail(testUser.getUserEmail()).orElseThrow();

        // Then
        Optional<UserEntity> find = userRepo.findByUserId(user.getUserId());
        checkEqualUser(find, testUser);
    }

    @Test
    @Transactional
    @DisplayName("유저 ID로 유저 찾기")
    void findByUserId() {
        // Given
        UserEntity save = userRepo.saveNewUser(testUser);

        // When
        Optional<UserEntity> find = userRepo.findByUserId(save.getUserId());
        checkEqualUser(find, save);
    }

    @Test
    @Transactional
    @DisplayName("유저 이메일로 유저 찾기")
    void findByUserEmail() {
        // Given
        userRepo.saveNewUser(testUser);

        // When
        Optional<UserEntity> find = userRepo.findByUserEmail(testUser.getUserEmail());
        checkEqualUser(find, testUser);
    }

    @Test
    @Transactional
    @DisplayName("유저 이메일과 비밀번호로 유저 찾기 / 로그인")
    void findByUserEmailAndPassword() {
        // Given
        UserEntity save = userRepo.saveNewUser(testUser);

        // When
        Optional<UserEntity> find = userRepo.findByUserEmailAndPassword(testUser.getUserEmail(), testUser.getUserPw());
        checkEqualUser(find, testUser);
    }

    @Test
    @Transactional
    @DisplayName("해당 역할의 유저들 찾기")
    void findByUserRole() {
        // Given
        userRepo.saveNewUser(testUser);
        userRepo.saveNewUser(testUser2);

        // When
        List<UserEntity> findList1 = userRepo.findByUserRole(UserRole.USER);
        List<UserEntity> findList2 = userRepo.findByUserRole(UserRole.ADMIN);

        // Then
        for (UserEntity find : findList1) {
            checkEqualUser2(find, testUser);
        }

        for (UserEntity find : findList2) {
            checkEqualUser2(find, testUser2);
        }

    }

    @Test
    @Transactional
    @DisplayName("전체 유저 목록 조회")
    void findAll() {
        // Given
        userRepo.saveNewUser(testUser);
        userRepo.saveNewUser(testUser2);

        // When
        List<UserEntity> users = userRepo.findAll();

        // Then
        checkEqualUser2(users.get(0), testUser);
        checkEqualUser2(users.get(1), testUser2);
    }

    @Test
    @Transactional
    @DisplayName("유저 정보 수정하기")
    void editUserInfo() {
        // Given
        UserEntity save = userRepo.saveNewUser(testUser);

        UserEntity editUser = UserEntity.builder()
                .userId(save.getUserId())
                .userEmail(testUser.getUserEmail())
                .userPw("editPw")
                .role(UserRole.USER)
                .alias("소소은")
                .phoneNum("010-1111-1111")
                .userName("정소은")
                .build();

        // When
        userRepo.editUserInfo(editUser);

        // Then
        Optional<UserEntity> find = userRepo.findByUserId(save.getUserId());
        checkEqualUser(find, editUser);
    }

    @Test
    @Transactional
    @DisplayName("유저 삭제하기")
    void deleteUser() {
        // Given
        userRepo.saveNewUser(testUser);

        // When
        UUID userId = testUser.getUserId();
        userRepo.deleteUser(userId);

        // Then
        assertThat(userRepo.findByUserId(userId).isEmpty());

    }

    private void checkEqualUser(Optional<UserEntity> find, UserEntity user) {
        assertThat(find.get().getUserId().equals(user.getUserId()));
        assertThat(find.get().getUserEmail().equals(user.getUserEmail()));
        assertThat(find.get().getUserPw().equals(user.getUserPw()));
        assertThat(find.get().getRole().equals(user.getRole()));
        assertThat(find.get().getAlias().equals(user.getAlias()));
        assertThat(find.get().getPhoneNum().equals(user.getPhoneNum()));
        assertThat(find.get().getUserName().equals(user.getUserName()));
    }

    private void checkEqualUser2 (UserEntity find, UserEntity user) {
        assertThat(find.getUserId().equals(user.getUserId()));
        assertThat(find.getUserEmail().equals(user.getUserEmail()));
        assertThat(find.getUserPw().equals(user.getUserPw()));
        assertThat(find.getRole().equals(user.getRole()));
        assertThat(find.getAlias().equals(user.getAlias()));
        assertThat(find.getPhoneNum().equals(user.getPhoneNum()));
        assertThat(find.getUserName().equals(user.getUserName()));
    }
}
