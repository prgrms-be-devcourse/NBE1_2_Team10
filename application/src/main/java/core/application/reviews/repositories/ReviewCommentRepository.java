package core.application.reviews.repositories;


import core.application.reviews.models.entities.ReviewCommentEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@code REVIEW_COMMENT_TABLE} 과 관련된 {@code Repository}
 */
public interface ReviewCommentRepository {

    //<editor-fold desc="CREATE">

    /**
     * 새로운 포스팅 댓글을 DB 에 등록
     *
     * @param reviewComment 등록할 포스팅 댓글 정보
     * @return {@link ReviewCommentEntity} 등록된 정보
     */
    ReviewCommentEntity saveNewReviewComment(ReviewCommentEntity reviewComment);


    // 부모 댓글을 등록

    /**
     * 특정 포스팅에 주어진 유저 ID 로 부모 댓글을 등록
     *
     * @param reviewId      댓글을 등록할 영화 후기 포스팅 ID
     * @param userId        포스팅 댓글을 등록하는 유저 ID
     * @param reviewComment 등록할 포스팅 댓글 정보
     * @return {@link ReviewCommentEntity} 등록된 정보
     */
    ReviewCommentEntity saveNewParentReviewComment(Long reviewId, UUID userId, ReviewCommentEntity reviewComment);


    // 자식 댓글을 등록

    /**
     * 특정 포스팅 댓글에 주어진 유저 ID 로 자식 댓글을 등록
     *
     * @param groupId       부모 댓글의 포스팅 댓글 ID
     * @param userId        댓글을 등록하는 유저 ID
     * @param reviewComment 등록할 댓글 정보
     * @return {@link ReviewCommentEntity} 등록된 정보
     */
    ReviewCommentEntity saveNewChildReviewComment(Long groupId, UUID userId, ReviewCommentEntity reviewComment);
    //</editor-fold>


    //<editor-fold desc="READ">

    /**
     * 포스팅 댓글 ID 로 검색
     *
     * @param reviewCommentId 포스팅 댓글 ID
     * @return {@link Optional}{@code <}{@link ReviewCommentEntity}{@code >}
     */
    Optional<ReviewCommentEntity> findByReviewCommentId(Long reviewCommentId);


    //<editor-fold desc="부모 댓글 검색">

    /**
     * 특정 포스팅에 달린 모든 부모 댓글을 검색.
     * <p>
     * 즉, {@code groupId == null} 인 댓글만 검색.
     *
     * @param reviewId 검색할 포스팅 ID
     * @return {@link List}{@code <}{@link ReviewCommentEntity}{@code >}
     */
    List<ReviewCommentEntity> findParentCommentByReviewId(Long reviewId);

    /**
     * 특정 포스팅에 달린 모든 부모 댓글을 최신순으로 검색
     * <p>
     * 즉, {@code groupId == null} 인 댓글만 검색.
     *
     * @param reviewId 검색할 포스팅 ID
     * @return {@link List}{@code <}{@link ReviewCommentEntity}{@code >}
     */
    List<ReviewCommentEntity> findParentCommentByReviewIdOnDateDescend(Long reviewId);

    /**
     * 특정 포스팅에 달린 모든 부모 댓글을 좋아요 순으로 검색
     * <p>
     * 즉, {@code groupId == null} 인 댓글만 검색.
     *
     * @param reviewId 검색할 포스팅 ID
     * @return {@link List}{@code <}{@link ReviewCommentEntity}{@code >}
     */
    List<ReviewCommentEntity> findParentCommentByReviewIdOnLikeDescend(Long reviewId);
    //</editor-fold>


    /**
     * 특정 부모 댓글에 달린 자식 댓글들을 최신순으로 검색
     * <p>
     * 자식 댓글은 {@code groupId != null} 인 댓글들.
     *
     * @param groupId 부모 댓글의 ID
     * @return {@link List}{@code <}{@link ReviewCommentEntity}{@code >}
     */
    List<ReviewCommentEntity> findChildCommentsByGroupId(Long groupId);


    /**
     * DB 에 저장된 모든 부모 포스팅 댓글을 검색
     * <p>
     * 즉, {@code groupId == null} 인 댓글만 검색.
     *
     * @return {@link List}{@code <}{@link ReviewCommentEntity}{@code >}
     */
    List<ReviewCommentEntity> selectAllParentComments();

    /**
     * DB 에 저장된 모든 포스팅 댓글을 검색
     * <p>
     * 자식 댓글은 {@code groupId != null} 인 댓글들.
     *
     * @return {@link List}{@code <}{@link ReviewCommentEntity}{@code >}
     */
    List<ReviewCommentEntity> selectAll();
    //</editor-fold>


    // UPDATE

    /**
     * 특정 포스팅 댓글의 정보를 {@code replacement} 정보로 변경
     * <p>
     * 이 때 {@code content}, {@code commentRef} 만 {@code replacement} 의 것으로 변경.
     * {@code isUpdated} 는 자동으로 변경.
     *
     * @param reviewCommentId 정보 변경할 포스팅 댓글의 ID
     * @param replacement     변경할 정보
     * @return {@link ReviewCommentEntity} 변경된 정보
     */
    ReviewCommentEntity editReviewCommentInfo(Long reviewCommentId, ReviewCommentEntity replacement);
}