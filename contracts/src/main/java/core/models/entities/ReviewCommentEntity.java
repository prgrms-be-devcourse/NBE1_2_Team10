package core.models.entities;

import java.time.Instant;
import java.util.UUID;

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
