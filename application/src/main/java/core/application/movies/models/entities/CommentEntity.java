package core.application.movies.models.entities;

import core.application.users.models.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

import core.application.movies.models.dto.request.CommentWriteReqDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "comment_table")
public class CommentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	private String content;
	@Column(name = "`like`")
	private int like;
	private int dislike;
	private int rating;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id")
	private CachedMovieEntity movie; // 영화 API 에 따라 달라질 수 있음.

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", columnDefinition = "binary(16)")
	private UserEntity user;
	private Instant createdAt;

	public static CommentEntity of(CommentWriteReqDTO comment, CachedMovieEntity movie, UserEntity user) {
		return CommentEntity.builder()
			.content(comment.getContent())
			.like(0)
			.dislike(0)
			.rating(comment.getRating())
			.movie(movie)
			.user(user)
			.build();
	}

	@PrePersist
	public void prePersist() {
		this.createdAt = Instant.now();
	}

	public void isLiked() {
		this.like++;
	}

	public void cancelLike() {
		this.like--;
	}

	public void isDisliked() {
		this.dislike++;
	}

	public void cancelDislike() {
		this.dislike--;
	}
}
