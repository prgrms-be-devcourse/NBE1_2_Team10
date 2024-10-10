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

    private Long reviewId;
    private String title;
    private String content;
    private UUID userId;
    private String movieId;
    private int like;

    @Setter
    private Instant createdAt;

    @Setter
    private Instant updatedAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewEntity that = (ReviewEntity) o;
        return like == that.like && Objects.equals(reviewId, that.reviewId)
                && Objects.equals(title, that.title) && Objects.equals(content,
                that.content) && Objects.equals(userId, that.userId)
                && Objects.equals(movieId, that.movieId) && Objects.equals(
                createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(reviewId);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(content);
        result = 31 * result + Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(movieId);
        result = 31 * result + like;
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Objects.hashCode(updatedAt);
        return result;
    }
}
