package core.application.movies.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.application.movies.models.entities.CommentEntity;
import core.application.movies.repositories.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

	private final CommentMapper commentMapper;

	@Override
	public CommentEntity saveNewComment(String movieId, UUID userId, CommentEntity comment) {
		commentMapper.save(movieId, userId, comment);
		return comment;
	}

	@Override
	public Optional<CommentEntity> findByCommentId(Long commentId) {
		return commentMapper.findByCommentId(commentId);
	}

	@Override
	public List<CommentEntity> findByMovieId(String movieId) {
		return commentMapper.findByMovieId(movieId);
	}

	@Override
	public List<CommentEntity> findByMovieIdOnDateDescend(String movieId) {
		return commentMapper.findByMovieIdOnDateDescend(movieId);
	}

	@Override
	public List<CommentEntity> findByMovieIdOnLikeDescend(String movieId) {
		return commentMapper.findByMovieIdOnLikeDescend(movieId);
	}

	@Override
	public List<CommentEntity> findByMovieIdOnDislikeDescend(String movieId) {
		return commentMapper.findByMovieIdOnDislikeDescend(movieId);
	}

	@Override
	public List<CommentEntity> selectAll() {
		return commentMapper.selectAll();
	}

	@Override
	public void deleteComment(Long commentId) {
		commentMapper.delete(commentId);
	}
}
