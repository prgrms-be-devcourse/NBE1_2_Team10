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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(selectParentCommentOnReviewId)
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> findParentCommentByReviewId(Long reviewId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(selectParentCommentOnReviewId + " ORDER BY CREATED_AT DESC, REVIEW_COMMENT_ID DESC ")
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> findParentCommentByReviewIdOnDateDescend(Long reviewId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Select(selectParentCommentOnReviewId + " ORDER BY `like` DESC, REVIEW_COMMENT_ID DESC ")
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> findParentCommentByReviewIdOnLikeDescend(Long reviewId);


    /**
     * {@inheritDoc}
     */
    @Override
    @Select(" SELECT * FROM REVIEW_COMMENT_TABLE WHERE GROUP_ID = #{groupId} "
            + "ORDER BY CREATED_AT DESC, REVIEW_COMMENT_ID DESC")
    @ResultMap("ReviewCommentResultMap")
    List<ReviewCommentEntity> findChildCommentsByGroupId(Long groupId);


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

    @UpdateProvider(type = ReviewCommentMapperProvider.class, method = "editReviewComment")
    int updateReviewCommentEntity(
            @Param("reviewCommentId") Long reviewCommentId,
            @Param("replacement") ReviewCommentEntity replacement
    );

    /**
     * {@inheritDoc}
     *
     * @see ReviewCommentMapperProvider#editReviewComment
     */
    @Override
    @Transactional
    @UpdateProvider(type = ReviewCommentMapperProvider.class, method = "editReviewComment")
    default ReviewCommentEntity editReviewCommentInfo(Long reviewCommentId,
            ReviewCommentEntity replacement) {
        int result = updateReviewCommentEntity(reviewCommentId, replacement);
        return findByReviewCommentId(reviewCommentId).orElseThrow();
    }
}