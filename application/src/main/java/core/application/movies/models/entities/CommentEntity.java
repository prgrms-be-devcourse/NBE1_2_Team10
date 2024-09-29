package core.application.movies.models.entities;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class CommentEntity {
    private Long    commentId;
    private String  content;
    private int     like;
    private int     dislike;
    private int     rating;
    private String  movieId;     // 영화 API 에 따라 달라질 수 있음.
    private UUID    userId;
    private Instant createdAt;
}
