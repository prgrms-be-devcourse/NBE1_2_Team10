package core.application.reviews.repositories.mybatis;

import core.application.reviews.models.entities.ReviewCommentEntity;
import core.application.reviews.repositories.ReviewCommentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface MyBatisReviewCommentRepository extends ReviewCommentRepository {

    //<editor-fold desc="CREATE">

    /**
     * 실질적으로 DB 에 {@code insert} 하는 {@code MyBatis Query} 용 메서드
     *
     * @param data 삽입 데이터
     * @return {@code insert} 결과
     * @see ReviewCommentMapperProvider#insertReviewComment
     */
    @InsertProvider(type = ReviewCommentMapperProvider.class, method = "insertReviewComment")
    @Options(useGeneratedKeys = true, keyColumn = "review_comment_id", keyProperty = "reviewCommentId")
    int insertReviewComment(
            ReviewCommentEntity data
    );


    /**
     * {@inheritDoc}
     * <p>
     * 이 때 {@code reviewCommentId} 를 제외한 모든 내용을 그대로 DB 에 저장
     * <p>
     * 내부적으로 {@link #insertReviewComment} 를 이용
     *
     * @see #insertReviewComment
     */
    @Override
    default ReviewCommentEntity saveNewReviewComment(ReviewCommentEntity reviewComment) {
        int result = this.insertReviewComment(reviewComment);
        return reviewComment;
    }


    /**
     * {@inheritDoc}
     * <p>
     * 내부적으로 {@link #saveNewReviewComment} 를 이용
     *
     * @see #saveNewReviewComment
     * @see #insertReviewComment
     */
    @Override
    default ReviewCommentEntity saveNewParentReviewComment(
            Long reviewId, UUID userId, ReviewCommentEntity reviewComment
    ) {
        // 보무 댓글은 groupId null
        ReviewCommentEntity data = ReviewCommentEntity
                .builder()
                .reviewId(reviewId)         // 포스팅 ID
                .userId(userId)             // 유저 ID
                .content(reviewComment.getContent())
                .groupId(null)
                .commentRef(reviewComment.getCommentRef())  // TODO 부모 댓글 멘션 기능 허용?
                .like(0)
                .createdAt(Instant.now()
                        .truncatedTo(ChronoUnit.SECONDS))   // 댓글 생성 시간
                .build();

        return this.saveNewReviewComment(data);
    }


    /**
     * {@inheritDoc}
     * <p>
     * 내부적으로 {@link #saveNewReviewComment} 를 이용
     *
     * @see #saveNewReviewComment
     * @see #insertReviewComment
     */
    @Override
    default ReviewCommentEntity saveNewChildReviewComment(
            Long groupId, UUID userId, ReviewCommentEntity reviewComment
    ) {
        ReviewCommentEntity data = ReviewCommentEntity
                .builder()
                .reviewId(reviewComment.getReviewId())
                .userId(userId)         // 유저 ID
                .content(reviewComment.getContent())
                .groupId(groupId)       // 부모 ID
                .commentRef(reviewComment.getCommentRef())
                .like(0)
                .createdAt(Instant.now()
                        .truncatedTo(ChronoUnit.SECONDS))   // 댓글 생성 시간
                .build();

        return this.saveNewReviewComment(data);
    }

    //</editor-fold>


    /**
     * {@inheritDoc}
     */
    @Override
    @Select("SELECT * FROM REVIEW_COMMENT_TABLE WHERE REVIEW_COMMENT_ID = #{reviewCommentId}")
    @Results(id = "ReviewCommentResultMap", value = {
            @Result(property = "reviewCommentId", column = "review_comment_id"),
            @Result(property = "reviewId", column = "review_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "content", column = "content"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "commentRef", column = "comment_ref"),
            @Result(property = "like", column = "like"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "isUpdated", column = "is_updated"),
    })
    Optional<ReviewCommentEntity> findByReviewCommentId(Long reviewCommentId);

    final String selectParentCommentOnReviewId = " SELECT * FROM REVIEW_COMMENT_TABLE" +
            " WHERE REVIEW_ID = #{reviewId}" + " AND GROUP_ID IS NULL ";
    final String orderByDate = " ORDER BY CREATED_AT DESC, REVIEW_COMMENT_ID DESC ";
    final String orderByLikes = " ORDER BY `like` DESC, REVIEW_COMMENT_ID DESC ";
    final String pagingOffset = " LIMIT #{num} OFFSET #{offset} ";

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(selectParentCommentOnReviewId + orderByDate + pagingOffset)
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> findParentCommentByReviewId(
            @Param("reviewId") Long reviewId,
            @Param("offset") int offset,
            @Param("num") int num);

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(selectParentCommentOnReviewId + orderByDate + pagingOffset)
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> findParentCommentByReviewIdOnDateDescend(
            @Param("reviewId") Long reviewId,
            @Param("offset") int offset,
            @Param("num") int num);

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(selectParentCommentOnReviewId + orderByLikes + pagingOffset)
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> findParentCommentByReviewIdOnLikeDescend(
            @Param("reviewId") Long reviewId,
            @Param("offset") int offset,
            @Param("num") int num);


    /**
     * {@inheritDoc}
     */
    @Override
    @Select(" SELECT COUNT(*) FROM REVIEW_COMMENT_TABLE WHERE review_id = #{reviewId} AND group_id IS NULL ")
    long countParentCommentByReviewId(Long reviewId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(" SELECT * FROM REVIEW_COMMENT_TABLE WHERE group_id = #{groupId} " + orderByDate
            + pagingOffset)
    List<ReviewCommentEntity> findChildCommentsByGroupId(
            @Param("groupId") Long groupId,
            @Param("offset") int offset,
            @Param("num") int num);

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(" SELECT COUNT(*) FROM REVIEW_COMMENT_TABLE WHERE group_id = #{groupId} ")
    long countChildCommentByGroupId(Long groupId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(" SELECT * FROM REVIEW_COMMENT_TABLE WHERE GROUP_ID IS NULL ")
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> selectAllParentComments();

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(" SELECT * FROM REVIEW_COMMENT_TABLE ")
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> selectAll();


    /**
     * 실질적으로 DB 에 {@code update} 하는 {@code MyBatis Query} 용 메서드
     *
     * @param reviewCommentId 댓글 ID
     * @param replacement     변경할 정보
     * @param update          {@code is_updated} 에 설정할 정보
     * @return {@code update} 결과
     * @see ReviewCommentMapperProvider#insertReviewComment
     */
    @UpdateProvider(type = ReviewCommentMapperProvider.class, method = "editReviewComment")
    int updateReviewCommentEntity(
            @Param("reviewCommentId") Long reviewCommentId,
            @Param("replacement") ReviewCommentEntity replacement,
            @Param("isUpdated") boolean update
    );

    /**
     * {@inheritDoc}
     *
     * @deprecated use instead {@link #editReviewCommentInfo(Long, ReviewCommentEntity, boolean)}
     */
    @Override
    @Deprecated
    default ReviewCommentEntity editReviewCommentInfo(Long reviewCommentId,
            ReviewCommentEntity replacement) {
        return editReviewCommentInfo(reviewCommentId, replacement, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ReviewCommentEntity editReviewCommentInfo(Long reviewCommentId,
            ReviewCommentEntity replacement, boolean update) {
        int result = updateReviewCommentEntity(reviewCommentId, replacement, update);
        return findByReviewCommentId(reviewCommentId).orElseThrow();
    }

    /**
     * 실질적으로 DB 에 리뷰 좋아요 {@code update} 하는 {@code MyBatis Query} 용 메서드
     *
     * @param reviewCommentId 댓글 ID
     * @param likes           변경될 좋아요 수
     * @return {@code update} 결과
     * @see ReviewCommentMapperProvider#updateCommentLikes
     */
    @UpdateProvider(type = ReviewCommentMapperProvider.class, method = "updateCommentLikes")
    int updateCommentLikes(@Param("reviewCommentId") Long reviewCommentId,
            @Param("likes") int likes);

    /**
     * {@inheritDoc}
     */
    @Override
    default ReviewCommentEntity updateReviewCommentLikes(Long reviewCommentId, int likes) {
        int result = updateCommentLikes(reviewCommentId, likes);
        return findByReviewCommentId(reviewCommentId).orElseThrow();
    }
}