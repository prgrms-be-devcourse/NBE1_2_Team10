package core.application.movies.models.dto.response;

import core.application.users.models.entities.UserEntity;
import java.time.Instant;
import java.util.UUID;

import core.application.movies.models.entities.CommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "한줄평 작성 응답 정보")
@AllArgsConstructor
public class CommentRespDTO {
	@Schema(description = "한줄평 ID", example = "1")
	private Long commentId;

	@Schema(description = "한줄평 작성 내용", example = "정말 재밌는 영화네요.")
	private String content;

	@Schema(description = "좋아요 개수", example = "0")
	private int like;

	@Schema(description = "싫어요 개수", example = "0")
	private int dislike;

	@Schema(description = "평점", example = "9")
	private int rating;

	@Schema(description = "한줄평 남긴 영화 ID", example = "A-12345")
	private String movieId;

	@Schema(description = "한줄평을 작성한 유저", example = "AB12-CD345-EF68")
	private UUID userId;

	@Schema(description = "한줄평 작성 시간", example = "2024-10-05")
	private Instant createdAt;

	@Schema(description = "현재 사용자가 해당 한줄평 좋아요 여부", example = "false")
	private Boolean isLiked;

	@Schema(description = "현재 사용자가 해당 한줄평 싫어요 여부", example = "false")
	private Boolean isDisliked;

	public static CommentRespDTO of(CommentEntity comment, UserEntity user) {
		return CommentRespDTO.builder()
			.commentId(comment.getCommentId())
			.content(comment.getContent())
			.movieId(comment.getMovie().getMovieId())
			.userId(user.getUserId())
			.like(comment.getLike())
			.dislike(comment.getDislike())
			.rating(comment.getRating())
			.createdAt(comment.getCreatedAt())
			.isLiked(false)
			.isDisliked(false)
			.build();
	}
}
