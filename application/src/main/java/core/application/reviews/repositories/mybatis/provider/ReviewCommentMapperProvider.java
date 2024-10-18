package core.application.reviews.repositories.mybatis.provider;

import core.application.reviews.repositories.mybatis.mappers.*;
import org.apache.ibatis.jdbc.*;

/**
 * {@code REVIEW_COMMENT_TABLE} 와 관련된 {@code MyBatis} 쿼리 제공 클래스
 */
public class ReviewCommentMapperProvider {

    /**
     * {@code insertReviewComment} 메서드 {@code MyBatis} 쿼리 생성용 메서드
     *
     * @see ReviewCommentMapper#insertReviewComment
     */
    public String insertReviewComment() {
        // reviewCommentId, isUpdated 빼고 전부 set
        SQL query = new SQL()
                .INSERT_INTO("REVIEW_COMMENT_TABLE")
                .VALUES("review_id", "#{reviewId}")
                .VALUES("user_id", "#{userId}")
                .VALUES("content", "#{content}")
                .VALUES("group_id", "#{groupId}")
                .VALUES("comment_ref", "#{commentRef}")
                .VALUES("`like`", "#{like}")
                .VALUES("created_at", "#{createdAt}");

        return query.toString();
    }

    /**
     * {@code updateReviewCommentEntity} 메서드 {@code MyBatis} 쿼리 생성용 메서드
     *
     * @see ReviewCommentMapper#updateReviewCommentEntity
     */
    public String editReviewComment() {
        SQL query = new SQL()
                .UPDATE("REVIEW_COMMENT_TABLE")
                .SET("content = #{replacement.content}")
                .SET("comment_ref = #{replacement.commentRef}")
                .SET("is_updated = #{isUpdated}")
                .WHERE("review_comment_id = #{reviewCommentId}");

        return query.toString();
    }

    /**
     * {@code updateCommentLikes} 메서드 {@code MyBatis} 쿼리 생성용 메서드
     *
     * @see ReviewCommentMapper#updateCommentLikes
     */
    public String updateCommentLikes() {
        SQL query = new SQL()
                .UPDATE("REVIEW_COMMENT_TABLE")
                .SET("`like` = #{likes}")
                .WHERE("review_comment_id = #{reviewCommentId}");

        return query.toString();
    }
}
