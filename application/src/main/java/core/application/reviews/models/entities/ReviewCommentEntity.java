package core.application.reviews.models.entities;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ReviewCommentEntity {
    private Long    reviewCommentId;
    private Long    reviewId;
    private UUID    userId;
    private String  content;
    private Long    groupId;
    private Long    commentRef;
    private int     like;
    private Instant createdAt;
    private boolean isUpdated;
}
