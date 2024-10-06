package core.application.reviews.models.entities;

import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * {@code  ReviewRepository} 와 관련된 엔티티
 *
 * @see core.application.reviews.repositories.ReviewRepository
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ReviewEntity {
    private Long    reviewId;
    private String  title;
    private String  content;
    private UUID    userId;
    private String  movieId;
    private int     like;
    private Instant createdAt;
    private Instant updatedAt;

    private Set<UUID> likeUsers = new HashSet<>();

    /* builder 사용하려면...
     * public static ReviewEntity from(ReviewReqDTO dto) {
     *     return ReviewEntity.builder()
     *     ...
     *     .build();
     * }
     */

    // 좋아요 증가
    public ReviewEntity increaseLikes(UUID userId) {
        Set<UUID> updatedLikeUsers = new HashSet<>(likeUsers);
        updatedLikeUsers.add(userId);

        return new ReviewEntity(
                this.reviewId,
                this.title,
                this.content,
                this.userId,
                this.movieId,
                this.like + 1,
                this.createdAt,
                this.updatedAt,
                updatedLikeUsers
        );
    }

    // 좋아요 감소
    public ReviewEntity decreaseLikes(UUID userId) {
        Set<UUID> updatedLikeUsers = new HashSet<>(likeUsers);
        updatedLikeUsers.remove(userId);

        return new ReviewEntity(
                this.reviewId,
                this.title,
                this.content,
                this.userId,
                this.movieId,
                this.like - 1,
                this.createdAt,
                this.updatedAt,
                updatedLikeUsers
        );
    }

    // 제목을 수정한 새로운 객체 반환
    public ReviewEntity withTitle(String title) {
        return new ReviewEntity(
                this.reviewId,
                title,
                this.content,
                this.userId,
                this.movieId,
                this.like,
                this.createdAt,
                this.updatedAt,
                this.likeUsers
        );
    }

    // 본문을 수정한 새로운 객체 반환
    public ReviewEntity withContent(String content) {
        return new ReviewEntity(
                this.reviewId,
                this.title,
                content,
                this.userId,
                this.movieId,
                this.like,
                this.createdAt,
                this.updatedAt,
                this.likeUsers
        );
    }

    // 업데이트 시간을 수정한 새로운 객체 반환
    public ReviewEntity withUpdatedAt(Instant updatedAt) {
        return new ReviewEntity(
                this.reviewId,
                this.title,
                this.content,
                this.userId,
                this.movieId,
                this.like,
                this.createdAt,
                updatedAt,
                this.likeUsers
        );
    }
}
