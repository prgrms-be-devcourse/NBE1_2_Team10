package core.application.reviews.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.application.reviews.ReviewExceptionHandler.ResponseError;
import core.application.reviews.exceptions.InvalidCommentContentException;
import core.application.reviews.exceptions.NotCommentOwnerException;
import core.application.reviews.models.dto.request.CreateCommentReqDTO;
import core.application.reviews.models.dto.response.CreateCommentRespDTO;
import core.application.reviews.models.dto.response.EditCommentRespDTO;
import core.application.reviews.models.dto.response.MessageRespDTO;
import core.application.reviews.models.dto.response.ShowCommentsRespDTO;
import core.application.reviews.models.entities.ReviewCommentEntity;
import core.application.reviews.services.ReviewCommentService;
import core.application.reviews.services.ReviewCommentSortOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies/{movieId}/reviews/{reviewId}")
@Tag(name = "Review Comment", description = "영화 후기 포스팅 댓글과 관련된 API")
@Transactional(readOnly = true)
public class ReviewCommentController {

	private final ReviewCommentService reviewCommentService;

	private static final int COMMENTS_PER_PAGE = 10;

	private static final String COOKIE_NAME = "RCLDA";

	/**
	 * 부모 댓글 보여주는 앤드포인트
	 *
	 * @param reviewId {@code pathVariable}
	 * @param page     페이징 넘버
	 * @return 응답용 댓글 목록들
	 */
	@GetMapping("/comments")
	@Operation(summary = "부모 댓글 조회", description = "특정 게시글의 부모 댓글을 페이징 하여 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공적으로 조회하였습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShowCommentsRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "Review ID 에 게시글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class))),
	})
	@Parameters({
		@Parameter(name = "reviewId", description = "댓글을 조회할 게시글 ID", example = "20"),
		@Parameter(name = "page", description = "0 보다 큰 페이징 넘버", example = "1")
	})
	public ShowCommentsRespDTO showParentReviewComments(
		@PathVariable("reviewId") Long reviewId,
		@RequestParam("page") int page) {

		// TODO `page` 양수인 것만 오도록 exception 처리 필요
		// 쿼리 파람에 @Positive 붙이고 컨트롤러에 @Validated 붙이면 될 듯?

		int offset = (page - 1) * COMMENTS_PER_PAGE;

		List<ReviewCommentEntity> parentReviewComments = reviewCommentService.getParentReviewComments(
			reviewId, ReviewCommentSortOrder.LIKE, offset, COMMENTS_PER_PAGE);

		return new ShowCommentsRespDTO().addComments(parentReviewComments);
	}

	/**
	 * 자식 댓글 보여주는 앤드포인트
	 *
	 * @param reviewId {@code pathVariable}
	 * @param groupId  {@code pathVariable}
	 * @param page     페이징 넘버
	 * @return 응답용 댓글 목록들
	 */
	@GetMapping("/comments/{groupId}")
	@Operation(summary = "자식 댓글 조회", description = "특정 게시글 속 부모 댓글의 자식 댓글을 페이징 하여 조회")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공적으로 조회하였습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShowCommentsRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "게시글 혹은 부모 댓글을 찾을 수 없습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class)))
	})
	@Parameters({
		@Parameter(name = "reviewId", description = "댓글을 조회할 게시글 ID", example = "20"),
		@Parameter(name = "groupId", description = "부모 댓글의 ID", example = "10010"),
		@Parameter(name = "page", description = "0 보다 큰 페이징 넘버", example = "1")
	})
	public ShowCommentsRespDTO showChildComments(
		@PathVariable("reviewId") Long reviewId,
		@PathVariable("groupId") Long groupId,
		@RequestParam("page") int page
	) {

		// TODO `page` 양수인 것만 오도록 exception 처리 필요
		// 쿼리 파람에 @Positive 붙이고 컨트롤러에 @Validated 붙이면 될 듯?

		int offset = (page - 1) * COMMENTS_PER_PAGE;

		List<ReviewCommentEntity> childReviewComments = reviewCommentService.getChildReviewCommentsOnParent(
			reviewId, groupId, offset, COMMENTS_PER_PAGE);

		return new ShowCommentsRespDTO().addComments(childReviewComments);
	}

	/**
	 * 댓글 생성하는 엔드포인트
	 *
	 * @param reviewId {@code pathVariable}
	 * @param dtoReq   요청 {@code DTO}
	 * @return 응답 {@code DTO}
	 */
	@PostMapping("/comments")
	@Operation(summary = "댓글 생성", description = "특정 게시글에 댓글을 생성")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공적으로 댓글을 생성하였습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCommentRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "게시글, 댓글을 찾을 수 없거나 잘못된 요청 body 가 들어왔습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class)))
	})
	@Parameter(name = "reviewId", description = "댓글을 조회할 게시글 ID", example = "20")
	public CreateCommentRespDTO createComment(
		@PathVariable("reviewId") Long reviewId,
		@RequestBody @Validated CreateCommentReqDTO dtoReq,
		BindingResult bindingResult
	) {

		if (bindingResult.hasErrors()) {
			throw new InvalidCommentContentException(
				bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		// TODO 추후 spring security context 에서 유저 아이디 받아야 함.
		// TODO_IMP 추후 spring security context 에서 유저 아이디 받아야 함.
		UUID userId = UUID.fromString("74062e0a-7fb6-11ef-95a5-00d861a152a7");

		Long groupId = dtoReq.getGroupId();
		ReviewCommentEntity validData = dtoReq.toEntity(userId);

		ReviewCommentEntity result = groupId == null ?
			reviewCommentService.addNewParentReviewComment(reviewId, userId, validData) :
			reviewCommentService.addNewChildReviewComment(reviewId, groupId, userId, validData);

		return CreateCommentRespDTO.toDTO(result);
	}

	/**
	 * 댓글 수정하는 엔드포인트
	 *
	 * @param reviewCommentId {@code pathVariable}
	 * @param dtoReq          요청 {@code DTO}
	 * @return 응답 {@code DTO}
	 */
	@Operation(summary = "댓글 수정", description = "작성한 댓글을 수정")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공적으로 댓글 수정", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditCommentRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "댓글 작성자 X or 존재하지 않는 댓글 or 잘못된 작성 시도", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class)))
	})
	@PatchMapping("/comments/{reviewCommentId}")
	public EditCommentRespDTO editComment(
		@PathVariable("reviewCommentId") Long reviewCommentId,
		@RequestBody @Validated CreateCommentReqDTO dtoReq,
		BindingResult bindingResult
	) {

		if (bindingResult.hasErrors()) {
			throw new InvalidCommentContentException(
				bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		// TODO 추후 spring security context 에서 유저 아이디 받아야 함.
		// TODO_IMP 추후 spring security context 에서 유저 아이디 받아야 함.
		UUID userId = UUID.fromString("74062e0a-7fb6-11ef-95a5-00d861a152a7");

		if (!reviewCommentService.doesUserOwnsComment(userId, reviewCommentId)) {
			throw new NotCommentOwnerException("Only comment owner can edit comments");
		}

		Long commentRef = dtoReq.getCommentRef();
		String content = dtoReq.getContent();

		ReviewCommentEntity result = reviewCommentService.editReviewComment(reviewCommentId,
			commentRef, content);

		return EditCommentRespDTO.toDTO(result);
	}

	/**
	 * 댓글 삭제하는 엔드포인트
	 *
	 * @param reviewCommentId {@code pathVariable}
	 * @return 응답 {@code DTO}
	 */
	@Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "성공적으로 댓글 삭제", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "댓글 작성자 X or 존재하지 않는 댓글 삭제 시도", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class)))
	})
	@DeleteMapping("/comments/{reviewCommentId}")
	public MessageRespDTO deleteComment(
		@PathVariable("reviewCommentId") Long reviewCommentId) {

		// TODO 추후 spring security context 에서 유저 아이디 받아야 함.
		// TODO_IMP 추후 spring security context 에서 유저 아이디 받아야 함.
		UUID userId = UUID.fromString("74062e0a-7fb6-11ef-95a5-00d861a152a7");

		if (!reviewCommentService.doesUserOwnsComment(userId, reviewCommentId)) {
			throw new NotCommentOwnerException("Only comment owner can delete comments");
		}

		reviewCommentService.deleteReviewComment(reviewCommentId);

		return new MessageRespDTO("성공적으로 댓글을 삭제했습니다.");
	}

	/**
	 * 좋아요 증감시키는 엔드포인트
	 *
	 * @param reviewCommentId {@code pathVariable}
	 * @param cookie          좋아요 눌렀는지 안눌렀는지 확인용 쿠키
	 * @param resp            쿠키 저장하고 삭제할 {@code servletResponse}
	 * @return 응답용 {@code DTO}
	 */
	@Operation(summary = "댓글 좋아요 또는 좋아요 취소")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "댓글 좋아요 또는 취소 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageRespDTO.class))),
		@ApiResponse(responseCode = "400", description = "존재하지 않는 댓글에 시도", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseError.class)))
	})
	@PatchMapping("/comments/{reviewCommentId}/like")
	public MessageRespDTO editLikes(
		@PathVariable("reviewCommentId") Long reviewCommentId,
		@CookieValue(value = COOKIE_NAME, required = false) Cookie cookie,
		HttpServletResponse resp) {

		// TODO 로그인 된 유저만 좋아요 증감시킬 수 있어야 함.
		// TODO_IMP 로그인 된 유저만 좋아요 증감시킬 수 있어야 함.

		String resultMessage;
		ReviewCommentEntity entity;

		if (cookie != null && cookie.getValue().equals(String.valueOf(reviewCommentId))) {
			resultMessage = "댓글의 좋아요를 감소시켰습니다.";
			entity = reviewCommentService.decreaseCommentLike(reviewCommentId);
			deleteCookie(reviewCommentId, resp);
		} else {
			resultMessage = "댓글의 좋아요를 증가시켰습니다. ";
			entity = reviewCommentService.increaseCommentLike(reviewCommentId);
			saveCookie(reviewCommentId, resp);
		}

		resultMessage += " [" + entity.getLike() + "]";

		return new MessageRespDTO(resultMessage);
	}

	private void saveCookie(Long reviewCommentId, HttpServletResponse resp) {
		Cookie cookie = new Cookie(COOKIE_NAME, String.valueOf(reviewCommentId));

		int year = 365 * 24 * 60 * 60;
		cookie.setMaxAge(year);
		cookie.setHttpOnly(true);

		resp.addCookie(cookie);
	}

	private void deleteCookie(Long reviewCommentId, HttpServletResponse resp) {
		Cookie cookie = new Cookie(COOKIE_NAME, String.valueOf(reviewCommentId));
		cookie.setMaxAge(0);
		resp.addCookie(cookie);
	}
}
