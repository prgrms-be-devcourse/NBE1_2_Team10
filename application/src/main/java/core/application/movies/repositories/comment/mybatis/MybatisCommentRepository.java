package core.application.movies.repositories.comment.mybatis;

import core.application.movies.repositories.comment.CommentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import core.application.movies.models.dto.response.CommentRespDTO;
import core.application.movies.models.entities.CommentEntity;
import core.application.movies.repositories.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Profile("mybatis")
public class MybatisCommentRepository implements CommentRepository {

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
	public Page<CommentRespDTO> findByMovieIdOrderBy(String movieId, UUID userId, Pageable pageable) {
		return null;
	}

	@Override
	public List<CommentEntity> selectAll() {
		return commentMapper.selectAll();
	}

	@Override
	public long countByMovieId(String movieId) {
		return commentMapper.countByMovieId(movieId);
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
