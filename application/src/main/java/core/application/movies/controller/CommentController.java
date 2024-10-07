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
import core.application.movies.exception.ExceptionResult;
import core.application.movies.exception.WrongWriteCommentException;
import core.application.movies.models.dto.CommentReactionRespDTO;
import core.application.movies.models.dto.CommentRespDTO;
import core.application.movies.models.dto.CommentWriteReqDTO;
import core.application.movies.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
@Tag(name = "Comment", description = "한줄평 관련 API")
public class CommentController {
	private final CommentService commentService;

	@Operation(summary = "한줄평 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "한줄평 조회 성공",
			content = @Content(schema = @Schema(implementation = CommentRespDTO.class)))
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지", example = "0"),
		@Parameter(name = "sortType", description = "정렬 타입", example = "LIKE")
	})
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

	@Operation(summary = "한줄평 작성")
	@ApiResponses({
		@ApiResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = CommentRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "특정 영화의 한줄평 2회 이상 시도 or 한줄평 내용 공백 or 평점 범위 초과",
			content = @Content(schema = @Schema(implementation = ExceptionResult.class)))
	})
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

	@Operation(summary = "한줄평 좋아요")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CommentReactionRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "이미 좋아요한 한줄평 좋아요 시도 or 존재하지 않는 한줄평 좋아요 시도", content = @Content(schema = @Schema(implementation = ExceptionResult.class)))
	})
	@PostMapping("/{movieId}/comments/{commentId}/like")
	public CommentReactionRespDTO incrementCommentLike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.incrementCommentLike(commentId, null);
		return new CommentReactionRespDTO("한줄평 좋아요 완료");
	}

	@Operation(summary = "한줄평 좋아요 취소")
	@ApiResponses({
		@ApiResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = CommentReactionRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "좋아요 하지 않은 한줄평 좋아요 취소 시도 or 존재하지 않는 한줄평 좋아요 취소 시도", content = @Content(schema = @Schema(implementation = ExceptionResult.class)))
	})
	@DeleteMapping("/{movieId}/comments/{commentId}/like")
	public CommentReactionRespDTO decrementCommentLike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.decrementCommentLike(commentId, null);
		return new CommentReactionRespDTO("한줄평 좋아요 취소 완료");
	}

	@Operation(summary = "한줄평 싫어요")
	@ApiResponses({
		@ApiResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = CommentReactionRespDTO.class))),
		@ApiResponse(responseCode = "400",
			description = "이미 '싫어요'한 한줄평 '싫어요' 시도 or 존재하지 않는 한줄평 '싫어요' 시도",
			content = @Content(schema = @Schema(implementation = ExceptionResult.class)))
	})
	@PostMapping("/{movieId}/comments/{commentId}/dislike")
	public CommentReactionRespDTO incrementCommentDislike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.incrementCommentDislike(commentId, null);
		return new CommentReactionRespDTO("한줄평 싫어요 취소 완료");
	}

	@Operation(summary = "한줄평 싫어요 취소")
	@ApiResponses({
		@ApiResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = CommentReactionRespDTO.class))),
		@ApiResponse(responseCode = "400",
			description = "싫어요 하지 않은 한줄평 좋아요 취소 시도 or 존재하지 않는 한줄평 싫어요 취소 시도",
			content = @Content(schema = @Schema(implementation = ExceptionResult.class)))
	})
	@DeleteMapping("/{movieId}/comments/{commentId}/dislike")
	public CommentReactionRespDTO decrementCommentDislike(@PathVariable Long commentId) {
		// 추후 JWT 구현 시 userId 삽입
		commentService.decrementCommentDislike(commentId, null);
		return new CommentReactionRespDTO("한줄평 좋아요 완료");
	}
}
