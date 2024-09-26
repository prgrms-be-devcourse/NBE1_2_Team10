package core.application.reviews.models.entities;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ReviewEntity {
    private Long    reviewId;
    private String  title;
    private String  content;
    private UUID    userId;
    private String  movieId;
    private int     like;
    private Instant createdAt;
    private Instant updatedAt;
}
