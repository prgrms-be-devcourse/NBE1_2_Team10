package core.application.movies.repositories.comment.jpa;

import core.application.movies.models.dto.response.CommentRespDTO;
import core.application.movies.models.entities.CommentEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {
    Boolean existsByMovieIdAndUserId(String movieId, UUID userId);

    @Query("SELECT new core.application.movies.models.dto.response.CommentRespDTO(c.commentId, c.content, c.like, c.dislike, c.rating, c.movieId, c.userId, c.createdAt, " +
            "CASE WHEN l.commentLikeId IS NOT NULL THEN true ELSE false END, " +
            "CASE WHEN d.commentDislikeId IS NOT NULL THEN true ELSE false END) " +
            "FROM CommentEntity c " +
            "LEFT JOIN CommentLike l ON c.commentId = l.comment.commentId AND l.userId = :userId " +
            "LEFT JOIN CommentDislike d ON c.commentId = d.comment.commentId AND d.userId = :userId " +
            "WHERE c.movieId = :movieId")
    Page<CommentRespDTO> findByMovieId(String movieId, UUID userId, Pageable pageable);

    @Query("SELECT new core.application.movies.models.dto.response.CommentRespDTO(c.commentId, c.content, c.like, c.dislike, c.rating, c.movieId, c.userId, c.createdAt, " +
            "CASE WHEN l.commentLikeId IS NOT NULL THEN true ELSE false END, " +
            "CASE WHEN d.commentDislikeId IS NOT NULL THEN true ELSE false END) " +
            "FROM CommentEntity c " +
            "LEFT JOIN CommentLike l ON c.commentId = l.comment.commentId AND l.userId = :userId " +
            "LEFT JOIN CommentDislike d ON c.commentId = d.comment.commentId AND d.userId = :userId " +
            "WHERE c.movieId = :movieId")
    Page<CommentRespDTO> findByMovieIdOrderBy(String movieId, UUID userId, Pageable pageable);
}
