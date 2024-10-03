package core.application.users.repositories;

import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@code USER_TABLE} 과 관련된 {@code Repository} 인터페이스
 * 사용자 데이터에 대한 CRUD(Create, Read, Update, Delete) 작업을 수행하기 위한 메서드를 정의
 */
public interface UserRepository {

    // CREATE

    /**
     * 새로운 사용자를 DB 에 등록
     *
     * @param newUser 새 사용자 정보
     * @return {@link int} 등록된 사용자의 ID 또는 영향을 받은 행 수
     */
    int saveNewUser(UserEntity newUser);


    //<editor-fold desc="READ">

    /**
     * 사용자 ID 로 검색
     *
     * @param userId ID
     * @return {@link Optional}{@code <}{@link UserEntity}{@code >} 해당 이메일을 가진 사용자의 정보
     */
    Optional<UserEntity> findByUserId(UUID userId);

    /**
     * 사용자 이메일로 검색
     *
     * @param email 이메일
     * @return {@link Optional}{@code <}{@link UserEntity}{@code >}
     */
    Optional<UserEntity> findByUserEmail(String email);

    /**
     * 사용자 이메일, 비밀번호로 검색
     *
     * @param email    사용자 이메일
     * @param password 비밀번호
     * @return {@link Optional}{@code <}{@link UserEntity}{@code >} 해당 이메일과 비밀번호를 가진 사용자의 정보
     */
    Optional<UserEntity> findByUserEmailAndPassword(String email, String password);

    /**
     * 특정 권한을 가진 모든 사용자를 검색
     *
     * @param role 사용자 권한
     * @return {@link List}{@code <}{@link UserEntity}{@code >} 특정 권한을 가진 사용자 {@code List}
     */
    List<UserEntity> findByUserRole(UserRole role);

    /**
     * 특정 사용자의 찜 목록을 load 한 {@code UserEntity} 반환
     *
     * @param userId 사용자 ID
     * @return {@link List}{@code <}{@link UserEntity}{@code >} {@code dibEntityList} 가 채워진 {@code UserEntity List}
     */
    List<DibEntity> selectDibsOnUserId(UUID userId);

    /**
     * DB 의 모든 사용자를 검색
     *
     * @return {@link List}{@code <}{@link UserEntity}{@code >}
     */
    List<UserEntity> findAll();
    //</editor-fold>


    // UPDATE

    /**
     * 특정 ID 의 사용자 정보를 {@code replacement} 정보로 변경
     * 이 때 {@code userId} 를 제외한 모든 정보가 {@code replacement}의 것으로 변경
     *
     * @param replacement 변경할 유저 정보
     *      * @return {@link int} 업데이트된 행 수
     */
    int editUserInfo(UserEntity replacement);


    // DELETE

    /**
     * 특정 ID의 사용자를 데이터베이스에서 삭제
     *
     * @param userId 삭제할 유저 ID
     *      * @return {@link int} 삭제된 행 수
     */
    int deleteUser(UUID userId);
}
