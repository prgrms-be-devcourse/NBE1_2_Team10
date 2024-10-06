package core.application.movies.models.dto;

import java.time.Instant;
import java.util.UUID;

import core.application.movies.models.entities.CommentEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRespDTO {
	private Long commentId;
	private String content;
	private int like;
	private int dislike;
	private int rating;
	private String movieId;
	private UUID userId;
	private Instant createdAt;
	private Boolean isLiked;
	private Boolean isDisliked;

	public static CommentRespDTO from(CommentEntity comment) {
		return CommentRespDTO.builder()
			.commentId(comment.getCommentId())
			.movieId(comment.getMovieId())
			.content(comment.getContent())
			.userId(comment.getUserId())
			.like(comment.getLike())
			.dislike(comment.getDislike())
			.rating(comment.getDislike())
			.createdAt(comment.getCreatedAt())
			.build();
	}
}
