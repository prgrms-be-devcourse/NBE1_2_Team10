package core.application.movies.repositories.comment;

import core.application.movies.models.dto.response.CommentRespDTO;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import core.application.movies.models.entities.CommentEntity;
import org.springframework.data.jpa.repository.Query;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {
	@Query("select c from CachedMovieEntity c "
			+ "join CachedMovieEntity m on c.movieId = m.movieId")
	Optional<CommentEntity> findByCommentIdAAndMovie_MovieId(Long commentId);

	Boolean existsByMovie_MovieIdAndUser_UserId(String movieId, UUID userId);

	@Query("SELECT new core.application.movies.models.dto.response.CommentRespDTO(c.commentId, c.content, c.like, c.dislike, c.rating, c.movie.movieId, c.user.userId, c.createdAt, " +
			"CASE WHEN l.commentLikeId IS NOT NULL THEN true ELSE false END, " +
			"CASE WHEN d.commentDislikeId IS NOT NULL THEN true ELSE false END) " +
			"FROM CommentEntity c " +
			"LEFT JOIN CommentLike l ON c.commentId = l.comment.commentId AND l.userId = :userId " +
			"LEFT JOIN CommentDislike d ON c.commentId = d.comment.commentId AND d.userId = :userId " +
			"WHERE c.movie.movieId = :movieId")
	Page<CommentRespDTO> findByMovieId(String movieId, UUID userId, Pageable pageable);

	@Query("SELECT new core.application.movies.models.dto.response.CommentRespDTO(c.commentId, c.content, c.like, c.dislike, c.rating, c.movie.movieId, c.user.userId, c.createdAt, " +
			"CASE WHEN l.commentLikeId IS NOT NULL THEN true ELSE false END, " +
			"CASE WHEN d.commentDislikeId IS NOT NULL THEN true ELSE false END) " +
			"FROM CommentEntity c " +
			"LEFT JOIN CommentLike l ON c.commentId = l.comment.commentId AND l.userId = :userId " +
			"LEFT JOIN CommentDislike d ON c.commentId = d.comment.commentId AND d.userId = :userId " +
			"WHERE c.movie.movieId = :movieId "
			+ "ORDER BY c.createdAt DESC")
	Page<CommentRespDTO> findByMovieIdOrderByCreatedAt(String movieId, UUID userId, Pageable pageable);

	@Query("SELECT new core.application.movies.models.dto.response.CommentRespDTO(c.commentId, c.content, c.like, c.dislike, c.rating, c.movie.movieId, c.user.userId, c.createdAt, " +
			"CASE WHEN l.commentLikeId IS NOT NULL THEN true ELSE false END, " +
			"CASE WHEN d.commentDislikeId IS NOT NULL THEN true ELSE false END) " +
			"FROM CommentEntity c " +
			"LEFT JOIN CommentLike l ON c.commentId = l.comment.commentId AND l.userId = :userId " +
			"LEFT JOIN CommentDislike d ON c.commentId = d.comment.commentId AND d.userId = :userId " +
			"WHERE c.movie.movieId = :movieId "
			+ "ORDER BY c.like DESC")
	Page<CommentRespDTO> findByMovieIdOrderByLikeDesc(String movieId, UUID userId, Pageable pageable);

	@Query("SELECT new core.application.movies.models.dto.response.CommentRespDTO(c.commentId, c.content, c.like, c.dislike, c.rating, c.movie.movieId, c.user.userId, c.createdAt, " +
			"CASE WHEN l.commentLikeId IS NOT NULL THEN true ELSE false END, " +
			"CASE WHEN d.commentDislikeId IS NOT NULL THEN true ELSE false END) " +
			"FROM CommentEntity c " +
			"LEFT JOIN CommentLike l ON c.commentId = l.comment.commentId AND l.userId = :userId " +
			"LEFT JOIN CommentDislike d ON c.commentId = d.comment.commentId AND d.userId = :userId " +
			"WHERE c.movie.movieId = :movieId "
			+ "ORDER BY c.dislike DESC")
	Page<CommentRespDTO> findByMovieIdOrderByDislikeDesc(String movieId, UUID userId, Pageable pageable);
}
