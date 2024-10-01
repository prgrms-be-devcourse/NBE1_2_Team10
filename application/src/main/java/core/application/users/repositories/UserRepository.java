package core.application.users.repositories;

import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@code USER_TABLE} 과 관련된 {@code Repository}
 */
public interface UserRepository {

    // CREATE

    /**
     * 새로운 유저를 DB 에 등록
     *
     * @param newUser 새 유저 정보
     * @return {@link UserEntity} 등록된 유저 정보
     */
    int saveNewUser(UserEntity newUser);


    //<editor-fold desc="READ">

    /**
     * 유저 ID 로 검색
     *
     * @param userId ID
     * @return {@link Optional}{@code <}{@link UserEntity}{@code >}
     */
    Optional<UserEntity> findByUserId(UUID userId);

    /**
     * 유저 이메일로 검색
     *
     * @param email 이메일
     * @return {@link Optional}{@code <}{@link UserEntity}{@code >}
     */
    Optional<UserEntity> findByUserEmail(String email);

    /**
     * 유저 이메일, 비밀번호로 검색
     *
     * @param email    유저 이메일
     * @param password 비밀번호
     * @return {@link Optional}{@code <}{@link UserEntity}{@code >}
     */
    Optional<UserEntity> findByUserEmailAndPassword(String email, String password);

    /**
     * 특정 권한을 가진 모든 유저를 검색
     *
     * @param role 유저 권한
     * @return {@link List}{@code <}{@link UserEntity}{@code >} 특정 권한을 가진 유저 {@code List}
     */
    List<UserEntity> findByUserRole(UserRole role);

    /**
     * 특정 유저의 찜 목록을 load 한 {@code UserEntity} 반환
     *
     * @param userId 유저 ID
     * @return {@link List}{@code <}{@link UserEntity}{@code >} {@code dibEntityList} 가 채워진 {@code UserEntity List}
     */
    List<DibEntity> selectDibsOnUserId(UUID userId);

    /**
     * DB 의 모든 유저를 검색
     *
     * @return {@link List}{@code <}{@link UserEntity}{@code >}
     */
    List<UserEntity> findAll();
    //</editor-fold>


    // UPDATE

    /**
     * 특정 ID 의 유저 정보를 {@code replacement} 정보로 변경.
     * <p>
     * 이 때 {@code userId} 를 제외한 모든 정보가 {@code replacement} 의 것으로 변경.
     *
     * @param replacement 변경할 정보
     * @return {@link UserEntity} 변경된 정보
     */
    int editUserInfo(UserEntity replacement);


    // DELETE

    /**
     * 특정 ID 의 유저를 DB 에서 삭제.
     *
     * @param userId 삭제할 유저 ID
     */
    void deleteUser(UUID userId);
}
