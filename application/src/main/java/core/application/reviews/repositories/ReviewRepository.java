package core.application.reviews.repositories;


import core.application.reviews.models.entities.ReviewEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@code REVIEW_TABLE} 과 관련된 {@code Repository}
 */
public interface ReviewRepository {

    // CREATE

    /**
     * 특정 영화에 주어진 유저 ID 로 새로운 영화 후기 포스팅을 DB 에 등록
     *
     * @param movieId 후기 포스팅을 등록할 영화 ID
     * @param userId  포스팅을 등록하는 유저 ID
     * @param review  새로운 영화 후기 포스팅
     * @return {@link ReviewEntity} 등록된 정보
     */
    ReviewEntity saveNewReview(String movieId, UUID userId, ReviewEntity review);


    //<editor-fold desc="READ">

    /**
     * 후기 포스팅 ID 로 검색
     *
     * @param reviewId 후기 포스팅 ID
     * @return {@link Optional}{@code <}{@link ReviewEntity}{@code >}
     */
    Optional<ReviewEntity> findByReviewId(Long reviewId);

    //<editor-fold desc="특정 영화의 후기 포스팅들을 검색">

    /**
     * 특정 영화의 후기 포스팅들을 검색
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> findByMovieId(String movieId);

    /**
     * 특정 영화의 후기 포스팅들을 최신순으로 검색
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> findByMovieIdOnDateDescend(String movieId);

    /**
     * 특정 영화의 후기 포스팅들을 좋아요 순으로 검색
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> findByMovieIdOnLikeDescend(String movieId);
    //</editor-fold>

    //<editor-fold desc="특정 영화의 포스팅을 본문 없이 검색">

    /**
     * 특정 영화의 후기 포스팅들을 검색
     * <p>
     * 이 때 포스팅의 본문을 load 하지 않음.
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> findByMovieIdWithoutContent(String movieId);

    /**
     * 특정 영화의 후기 포스팅들을 최신순으로 검색
     * <p>
     * 이 때 포스팅의 본문을 load 하지 않음.
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> findByMovieIdWithoutContentOnDateDescend(String movieId);

    /**
     * 특정 영화의 후기 포스팅들을 좋아요 순으로 검색
     * <p>
     * 이 때 포스팅의 본문을 load 하지 않음.
     *
     * @param movieId 검색할 영화 ID
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> findByMovieIdWithoutContentOnLikeDescend(String movieId);
    //</editor-fold>

    /**
     * 특정 유저가 작성한 영화 후기 포스팅들을 검색
     *
     * @param userId 검색할 유저 ID
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> findByUserId(UUID userId);

    /**
     * DB 의 모든 영화 후기 포스팅을 검색
     *
     * @return {@link List}{@code <}{@link ReviewEntity}{@code >}
     */
    List<ReviewEntity> selectAll();
    //</editor-fold>


    // UPDATE

    /**
     * 특정 후기 포스팅의 정보를 {@code replacement} 정보로 변경.
     * <p>
     * 이 때 {@code title}, {@code content} 만 {@code replacement} 의 것으로 변경. {@code updatedAt} 은 자동으로 변경.
     *
     * @param reviewId    정보 변경할 포스팅의 ID
     * @param replacement 변경할 정보
     * @return {@link ReviewEntity} 변경된 정보
     */
    ReviewEntity editReviewInfo(Long reviewId, ReviewEntity replacement);


    // DELETE

    /**
     * 특정 후기 포스팅을 삭제
     *
     * @param reviewId 삭제할 포스팅 ID
     */
    void deleteReview(Long reviewId);
}
