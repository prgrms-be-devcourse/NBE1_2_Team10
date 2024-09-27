package core.application.reviews.models.entities;

import lombok.*;

import java.time.Instant;
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

    /* builder 사용하려면...
     * public static ReviewEntity from(ReviewReqDTO dto) {
     *     return ReviewEntity.builder()
     *     ...
     *     .build();
     * }
     */
}
