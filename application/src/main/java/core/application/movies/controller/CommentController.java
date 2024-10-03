package core.application.movies.controller;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.application.movies.constant.CommentSort;
import core.application.movies.exception.WrongWriteCommentException;
import core.application.movies.models.dto.CommentReactionRespDTO;
import core.application.movies.models.dto.CommentRespDTO;
import core.application.movies.models.dto.CommentWriteReqDTO;
import core.application.movies.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class CommentController {
	private final CommentService commentService;

	@GetMapping("/{movieId}/comments")
	public List<CommentRespDTO> getComments(@PathVariable String movieId, @RequestParam int page, @RequestParam
	String sortType) {
		/**
		 * 추후 JWT를 통해 userId 주입
		 * 잘못된 정렬 타입은 좋아요 순으로 제공한다.
		 */
		if (!CommentSort.isValid(sortType)) {
			return commentService.getComments(movieId, page, CommentSort.LIKE, null);
		}
		CommentSort sort = CommentSort.valueOf(sortType);
		return commentService.getComments(movieId, page, sort, null);
	}

	@PostMapping("/{movieId}/comments")
	public CommentRespDTO writeComment(@PathVariable String movieId,
		@RequestBody @Validated CommentWriteReqDTO writeReqDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.info("검증 오류 발생 : {}", bindingResult);
			throw new WrongWriteCommentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		// TODO JWT 토큰 구현 시, 유저 정보 추출 로직 필요
		return commentService.writeCommentOnMovie(writeReqDTO, null, movieId);
	}

	@PostMapping("/{movieId}/comments/{commentId}/like")
	public CommentReactionRespDTO incrementCommentLike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.incrementCommentLike(commentId, null);
		return new CommentReactionRespDTO("한줄평 좋아요 완료");
	}

	@DeleteMapping("/{movieId}/comments/{commentId}/like")
	public CommentReactionRespDTO decrementCommentLike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.decrementCommentLike(commentId, null);
		return new CommentReactionRespDTO("한줄평 좋아요 취소 완료");
	}

	@PostMapping("/{movieId}/comments/{commentId}/dislike")
	public CommentReactionRespDTO incrementCommentDislike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.incrementCommentDislike(commentId, null);
		return new CommentReactionRespDTO("한줄평 싫어요 취소 완료");
	}

	@DeleteMapping("/{movieId}/comments/{commentId}/dislike")
	public CommentReactionRespDTO decrementCommentDislike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.decrementCommentDislike(commentId, null);
		return new CommentReactionRespDTO("한줄평 좋아요 완료");
	}
}
