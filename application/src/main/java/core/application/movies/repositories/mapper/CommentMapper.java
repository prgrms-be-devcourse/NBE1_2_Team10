package core.application.movies.repositories.mapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;

import core.application.movies.models.entities.CommentEntity;

@Mapper
public interface CommentMapper {
	void save(String movieId, UUID userId, CommentEntity comment);

	Optional<CommentEntity> findByCommentId(Long commentId);

	List<CommentEntity> findByMovieId(String movieId);

	List<CommentEntity> findByMovieIdOnDateDescend(String movieId);

	List<CommentEntity> findByMovieIdOnLikeDescend(String movieId);

	List<CommentEntity> findByMovieIdOnDislikeDescend(String movieId);

	List<CommentEntity> selectAll();

	void delete(Long commentId);
}
