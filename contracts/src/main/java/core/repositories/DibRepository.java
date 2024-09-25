package core.repositories;

import core.models.entities.DibEntity;
import core.models.entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@code DIB_TABLE} 과 관련된 {@code Repository}
 */
public interface DibRepository {

    //<editor-fold desc="CREATE">

    /**
     * 새로운 찜 목록을 DB 에 등록
     *
     * @param dib 새로운 찜 목록
     * @return {@link DibEntity} 등록된 찜 정보
     */
    DibEntity saveNewDib(DibEntity dib);

    /**
     * 특정 유저에게 다수의 찜 목록을 DB 에 등록
     *
     * @param userId 찜 목록을 소유할 유저 ID
     * @param dibs   등록할 찜 목록들
     * @return {@link List}{@code <}{@link DibEntity}{@code >} 등록된 찜 정보들
     */
    List<DibEntity> saveNewDibs(UUID userId, List<DibEntity> dibs);
    //</editor-fold>


    //<editor-fold desc="READ">

    /**
     * 찜 목록 ID 로 검색
     *
     * @param id 찜 목록 ID
     * @return {@link Optional}{@code <}{@link DibEntity}{@code >}
     */
    Optional<DibEntity> findByDibId(Long id);

    /**
     * 특정 유저가 소유한 찜 목록을 검색
     *
     * @param userId 찜 목록을 소유한 유저 ID
     * @return {@link Optional}{@code <}{@link DibEntity}{@code >}
     * @see UserEntity#dibEntityList
     * @see UserRepository#selectDibsOnUserId(UUID)
     */
    List<DibEntity> findByUserId(UUID userId);

    /**
     * 특정 영화의 찜 된 횟수를 반환
     *
     * @param movieId 검색할 영화 ID
     * @return {@code Long} 해당 영화가 찜 된 횟수
     * @see DibEntity#movieId
     */
    Long countMovie(String movieId);

    /**
     * DB 의 모든 찜 목록을 검색
     *
     * @return {@link List}{@code <}{@link DibEntity}{@code >}
     */
    List<DibEntity> selectAll();
    //</editor-fold>


    //<editor-fold desc="DELETE">

    /**
     * 특정 찜 목록을 삭제
     *
     * @param dibId 삭제할 찜 목록의 ID
     */
    void deleteDib(Long dibId);

    /**
     * 특정 유저가 소유한 모든 찜 목록을 삭제
     *
     * @param userId 유저 ID
     */
    void deleteDib(UUID userId);
    //</editor-fold>
}
