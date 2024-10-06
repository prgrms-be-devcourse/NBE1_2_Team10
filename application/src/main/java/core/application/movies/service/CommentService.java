package core.application.movies.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.application.movies.constant.CommentSort;
import core.application.movies.exception.InvalidReactionException;
import core.application.movies.exception.NotFoundCommentException;
import core.application.movies.exception.WrongWriteCommentException;
import core.application.movies.models.dto.CommentRespDTO;
import core.application.movies.models.dto.CommentWriteReqDTO;
import core.application.movies.models.entities.CommentEntity;
import core.application.movies.repositories.CommentDislikeRepository;
import core.application.movies.repositories.CommentLikeRepository;
import core.application.movies.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final CommentLikeRepository likeRepository;
	private final CommentDislikeRepository dislikeRepository;

	@Transactional(readOnly = true)
	public List<CommentRespDTO> getComments(String movieId, int page, CommentSort sort, UUID userId) {
		if (sort.equals(CommentSort.LIKE)) {
			return commentRepository.findByMovieIdOnLikeDescend(movieId, userId, page * 10);
		}

		if (sort.equals(CommentSort.LATEST)) {
			return commentRepository.findByMovieIdOnDateDescend(movieId, userId, page * 10);
		}
		return commentRepository.findByMovieIdOnDislikeDescend(movieId, userId, page * 10);
	}

	@Transactional
	public CommentRespDTO writeCommentOnMovie(CommentWriteReqDTO writeReqDTO, UUID userId, String movieId) {
		// 이미 작성한 기록이 있는지 확인한다.
		if (commentRepository.existsByMovieIdAndUserId(movieId, userId)) {
			throw new WrongWriteCommentException("한줄평은 1회 작성만 가능합니다.");
		}

		CommentEntity newComment = CommentEntity.of(writeReqDTO, movieId, userId);
		CommentEntity save = commentRepository.saveNewComment(movieId, userId, newComment);
		return CommentRespDTO.from(save);
	}

	@Transactional
	public void incrementCommentLike(Long commentId, UUID userId) {
		CommentEntity comment = commentRepository.findByCommentId(commentId)
			.orElseThrow(() -> new NotFoundCommentException("존재하지 않는 한줄평입니다."));
		if (likeRepository.isExistLike(commentId, userId)) {
			throw new InvalidReactionException("이미 '좋아요'를 누른 한줄평입니다.");
		}

		comment.isLiked();
		commentRepository.update(comment);
		likeRepository.saveCommentLike(commentId, userId);
	}

	@Transactional
	public void decrementCommentLike(Long commentId, UUID userId) {
		CommentEntity comment = commentRepository.findByCommentId(commentId)
			.orElseThrow(() -> new NotFoundCommentException("존재하지 않는 한줄평입니다."));
		if (!likeRepository.isExistLike(commentId, userId)) {
			throw new InvalidReactionException("'좋아요'를 누르지 않은 한줄평입니다.");
		}

		comment.cancelLike();
		commentRepository.update(comment);
		likeRepository.deleteCommentLike(commentId, userId);
	}

	@Transactional
	public void incrementCommentDislike(Long commentId, UUID userId) {
		CommentEntity comment = commentRepository.findByCommentId(commentId)
			.orElseThrow(() -> new NotFoundCommentException("존재하지 않는 한줄평입니다."));
		if (dislikeRepository.isExistDislike(commentId, userId)) {
			throw new InvalidReactionException("이미 '싫어요'를 누른 한줄평입니다.");
		}

		comment.isDisliked();
		commentRepository.update(comment);
		dislikeRepository.saveCommentDislike(commentId, userId);
	}

	@Transactional
	public void decrementCommentDislike(Long commentId, UUID userId) {
		CommentEntity comment = commentRepository.findByCommentId(commentId)
			.orElseThrow(() -> new NotFoundCommentException("존재하지 않는 한줄평입니다."));
		if (!dislikeRepository.isExistDislike(commentId, userId)) {
			throw new InvalidReactionException("'싫어요'를 누르지 않은 한줄평입니다.");
		}

		comment.cancelDislike();
		commentRepository.update(comment);
		dislikeRepository.deleteCommentDislike(commentId, userId);
	}
}
