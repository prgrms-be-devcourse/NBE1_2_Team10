package core.application.reviews.models.entities;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@code ReviewCommentRepository} 와 관련된 엔티티
 *
 * @see core.application.reviews.repositories.ReviewCommentRepository
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ReviewCommentEntity {

    /**
     * 포스팅 리뷰 댓글 ID
     */
    private Long reviewCommentId;

    /**
     * 댓글이 달린 포스팅 ID
     */
    private Long reviewId;
    private UUID userId;
    private String content;

    /**
     * 부모 댓글의 ID, 자신이 부모 댓글이면 {@code Null}
     * <p>
     * {@link #reviewCommentId} 와 동일
     */
    private Long groupId;

    /**
     * 현 댓글이 멘션하는 댓글의 ID
     * <p>
     * {@link #reviewCommentId} 와 동일
     */
    private Long commentRef;
    private int like;

    @Setter
    private Instant createdAt;

    @Setter
    private boolean isUpdated;

    /**
     * 어느 댓글을 멘션하는 기능
     * <p>
     * {@code (자기자신) -> (target)}, 현재 엔티티가 {@code target} 을 멘션함.
     *
     * @param target 멘션 당할 {@code ReviewCommentEntity}
     * @return {@code this} 멘션 정보가 들어간 댓글 정보
     */
    public ReviewCommentEntity mentionReviewComment(ReviewCommentEntity target) {
        this.commentRef = target.getReviewCommentId();
        return this;
    }

    /**
     * 댓글에 좋아요를 증가시키는 기능
     *
     * @param num 증가시킬 좋아요 개수
     * @return {@code this} 좋아요가 증가된 댓글 정보
     */
    public ReviewCommentEntity increaseLikes(int num) {
        this.like += num;
        return this;
    }

    /**
     * 댓글에 누른 좋아요를 감소시키는 기능
     *
     * @param num 감소시킬 좋아요 개수
     * @return {@code this} 좋아요가 감소된 댓글 정보
     */
    public ReviewCommentEntity decreaseLikes(int num) {
        this.like -= num;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewCommentEntity that = (ReviewCommentEntity) o;
        return like == that.like && isUpdated == that.isUpdated && Objects.equals(
                reviewCommentId, that.reviewCommentId) && Objects.equals(reviewId, that.reviewId)
                && Objects.equals(userId, that.userId) && Objects.equals(content,
                that.content) && Objects.equals(groupId, that.groupId)
                && Objects.equals(commentRef, that.commentRef) && Objects.equals(
                createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(reviewCommentId);
        result = 31 * result + Objects.hashCode(reviewId);
        result = 31 * result + Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(content);
        result = 31 * result + Objects.hashCode(groupId);
        result = 31 * result + Objects.hashCode(commentRef);
        result = 31 * result + like;
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Boolean.hashCode(isUpdated);
        return result;
    }
}
