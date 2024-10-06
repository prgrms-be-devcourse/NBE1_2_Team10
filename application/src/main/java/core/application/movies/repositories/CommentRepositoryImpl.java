package core.application.movies.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.application.movies.models.dto.CommentRespDTO;
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
		return findByCommentId(comment.getCommentId()).orElse(null);
	}

	@Override
	public Optional<CommentEntity> findByCommentId(Long commentId) {
		return commentMapper.findByCommentId(commentId);
	}

	@Override
	public Boolean existsByMovieIdAndUserId(String movieId, UUID userId) {
		return commentMapper.findByMovieIdAndUserId(movieId, userId).isPresent();
	}

	@Override
	public List<CommentRespDTO> findByMovieId(String movieId, UUID userId, int offset) {
		return commentMapper.findByMovieId(movieId, userId, offset);
	}

	@Override
	public List<CommentRespDTO> findByMovieIdOnDateDescend(String movieId, UUID userId, int offset) {
		return commentMapper.findByMovieIdOnDateDescend(movieId, userId, offset);
	}

	@Override
	public List<CommentRespDTO> findByMovieIdOnLikeDescend(String movieId, UUID userId, int offset) {
		return commentMapper.findByMovieIdOnLikeDescend(movieId, userId, offset);
	}

	@Override
	public List<CommentRespDTO> findByMovieIdOnDislikeDescend(String movieId, UUID userId, int offset) {
		return commentMapper.findByMovieIdOnDislikeDescend(movieId, userId, offset);
	}

	@Override
	public List<CommentEntity> selectAll() {
		return commentMapper.selectAll();
	}

	@Override
	public void update(CommentEntity comment) {
		commentMapper.update(comment);
	}

	@Override
	public void deleteComment(Long commentId) {
		commentMapper.delete(commentId);
	}
}
