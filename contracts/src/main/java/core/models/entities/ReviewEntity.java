package core.models.entities;

import java.time.Instant;
import java.util.UUID;

public class ReviewEntity {
    private Long    reviewId;
    private String  title;
    private String  content;
    private UUID    userId;
    private String  movieId;     // 영화 API 에 따라 달라질 수 있음.
    private int     like;
    private Instant createdAt;
    private Instant updatedAt;
}
