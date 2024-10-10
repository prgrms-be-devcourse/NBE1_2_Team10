package core.application.reviews.controllers;

import core.application.api.response.ApiResponse;
import core.application.api.response.code.Message;
import core.application.reviews.exceptions.InvalidCommentContentException;
import core.application.reviews.exceptions.InvalidPageException;
import core.application.reviews.exceptions.NotCommentOwnerException;
import core.application.reviews.models.dto.request.CreateCommentReqDTO;
import core.application.reviews.models.dto.response.CreateCommentRespDTO;
import core.application.reviews.models.dto.response.EditCommentRespDTO;
import core.application.reviews.models.dto.response.ShowCommentsRespDTO;
import core.application.reviews.models.entities.ReviewCommentEntity;
import core.application.reviews.services.ReviewCommentService;
import core.application.reviews.services.ReviewCommentSortOrder;
import core.application.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movies/{movieId}/reviews/{reviewId}")
@Tag(name = "Review Comment", description = "영화 후기 포스팅 댓글과 관련된 API")
@Transactional(readOnly = true)
public class ReviewCommentController {

	private final ReviewCommentService reviewCommentService;

	private static final int COMMENTS_PER_PAGE = 10;

	private static final String COOKIE_NAME_PREFIX = "ReviewCommentLikeAdjustment";

	/**
	 * 부모 댓글 보여주는 앤드포인트
	 *
	 * @param reviewId {@code pathVariable}
	 * @param page     페이징 넘버
	 * @return 응답용 댓글 목록들
	 */
	@GetMapping("/comments")
	@Operation(summary = "부모 댓글 조회", description = "특정 게시글의 부모 댓글을 페이징 하여 조회")
	@Parameters({
		@Parameter(name = "reviewId", description = "댓글을 조회할 게시글 ID", example = "20"),
		@Parameter(name = "page", description = "0 보다 큰 페이징 넘버", example = "1")
	})
	public ApiResponse<ShowCommentsRespDTO> showParentReviewComments(
		@PathVariable("reviewId") Long reviewId,
			@RequestParam(value = "page", defaultValue = "0") int page) {

		if (page < 0) {
			throw new InvalidPageException("잘못된 댓글 페이지입니다.");
		}

		int offset = page * COMMENTS_PER_PAGE;

		List<ReviewCommentEntity> parentReviewComments = reviewCommentService.getParentReviewComments(
			reviewId, ReviewCommentSortOrder.LIKE, offset, COMMENTS_PER_PAGE);

		ShowCommentsRespDTO showCommentsRespDTO = new ShowCommentsRespDTO().addComments(parentReviewComments);
		return ApiResponse.onSuccess(showCommentsRespDTO);
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
	@Parameters({
		@Parameter(name = "reviewId", description = "댓글을 조회할 게시글 ID", example = "20"),
		@Parameter(name = "groupId", description = "부모 댓글의 ID", example = "10010"),
		@Parameter(name = "page", description = "0 보다 큰 페이징 넘버", example = "1")
	})
	public ApiResponse<ShowCommentsRespDTO> showChildComments(
		@PathVariable("reviewId") Long reviewId,
		@PathVariable("groupId") Long groupId,
			@RequestParam(value = "page", defaultValue = "0") int page
	) {

		if (page < 0) {
			throw new InvalidPageException("잘못된 댓글 페이지입니다.");
		}

		int offset = page * COMMENTS_PER_PAGE;

		List<ReviewCommentEntity> childReviewComments = reviewCommentService.getChildReviewCommentsOnParent(
			reviewId, groupId, offset, COMMENTS_PER_PAGE);

		ShowCommentsRespDTO showCommentsRespDTO = new ShowCommentsRespDTO().addComments(childReviewComments);
		return ApiResponse.onSuccess(showCommentsRespDTO);
	}

	/**
	 * 댓글 생성하는 엔드포인트
	 *
	 * @param reviewId {@code pathVariable}
	 * @param customUserDetails {@code Security context holder} 에 존재하는 유저 {@code principal}
	 * @param dtoReq   요청 {@code DTO}
	 * @return 응답 {@code DTO}
	 */
	@PostMapping("/comments")
	@Operation(summary = "댓글 생성", description = "특정 게시글에 댓글을 생성")
	@Parameter(name = "reviewId", description = "댓글을 조회할 게시글 ID", example = "20")
	public ApiResponse<CreateCommentRespDTO> createComment(
		@PathVariable("reviewId") Long reviewId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Validated CreateCommentReqDTO dtoReq,
		BindingResult bindingResult
	) {

		if (bindingResult.hasErrors()) {
			throw new InvalidCommentContentException(
				bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		// principal 로 부터 ID 받음
		UUID userId = customUserDetails.getUserId();

		Long groupId = dtoReq.getGroupId();
		ReviewCommentEntity validData = dtoReq.toEntity(userId);

		ReviewCommentEntity result = groupId == null ?
			reviewCommentService.addNewParentReviewComment(reviewId, userId, validData) :
			reviewCommentService.addNewChildReviewComment(reviewId, groupId, userId, validData);

		return ApiResponse.onCreateSuccess(CreateCommentRespDTO.toDTO(result));
	}

	/**
	 * 댓글 수정하는 엔드포인트
	 *
	 * @param reviewCommentId {@code pathVariable}
	 * @param customUserDetails {@code Security context holder} 에 존재하는 유저 {@code principal}
	 * @param dtoReq          요청 {@code DTO}
	 * @return 응답 {@code DTO}
	 */
	@Operation(summary = "댓글 수정", description = "작성한 댓글을 수정")
	@PatchMapping("/comments/{reviewCommentId}")
	public ApiResponse<EditCommentRespDTO> editComment(
		@PathVariable("reviewCommentId") Long reviewCommentId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody @Validated CreateCommentReqDTO dtoReq,
		BindingResult bindingResult
	) {

		if (bindingResult.hasErrors()) {
			throw new InvalidCommentContentException(
				bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		// principal 로 부터 ID 받음
		UUID userId = customUserDetails.getUserId();

		if (!reviewCommentService.doesUserOwnsComment(userId, reviewCommentId)) {
			throw new NotCommentOwnerException("Only comment owner can edit comments");
		}

		Long commentRef = dtoReq.getCommentRef();
		String content = dtoReq.getContent();

		ReviewCommentEntity result = reviewCommentService.editReviewComment(reviewCommentId,
			commentRef, content);

		return ApiResponse.onSuccess(EditCommentRespDTO.toDTO(result));
	}

	/**
	 * 댓글 삭제하는 엔드포인트
	 *
	 * @param reviewCommentId {@code pathVariable}
	 * @param customUserDetails {@code Security context holder} 에 존재하는 유저 {@code principal}
	 * @return 응답 {@code DTO}
	 */
	@Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제")
	@DeleteMapping("/comments/{reviewCommentId}")
	public ApiResponse<Message> deleteComment(
			@PathVariable("reviewCommentId") Long reviewCommentId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		// principal 로 부터 ID 받음
		UUID userId = customUserDetails.getUserId();

		if (!reviewCommentService.doesUserOwnsComment(userId, reviewCommentId)) {
			throw new NotCommentOwnerException("Only comment owner can delete comments");
		}

		reviewCommentService.deleteReviewComment(reviewCommentId);

		return ApiResponse.onDeleteSuccess(Message.createMessage("성공적으로 댓글을 삭제했습니다."));
	}

	/**
	 * 좋아요 증감시키는 엔드포인트
	 *
	 * @param reviewCommentId {@code pathVariable}
	 * @param userDetails {@code userEmail} 가져오기 위한 {@code principal}
	 * @param req              쿠키 가져올 {@code servletRequest}
	 * @param resp            쿠키 저장하고 삭제할 {@code servletResponse}
	 * @return 응답용 {@code DTO}
	 */
	@Operation(summary = "댓글 좋아요 또는 좋아요 취소")
	@PatchMapping("/comments/{reviewCommentId}/like")
	public ApiResponse<Message> editLikes(
		@PathVariable("reviewCommentId") Long reviewCommentId,
			@AuthenticationPrincipal CustomUserDetails userDetails,
			HttpServletRequest req, HttpServletResponse resp) {

		// hash 값 이용해서 쿠키 이름, 값 증빌할 거임
		String validCookieValue = String.valueOf(
				Objects.hash(reviewCommentId, userDetails.getUserEmail()));

		// request 내 쿠키 중 이름 일치하는 쿠키 확인
		Cookie cookie = req.getCookies() == null ? null :
				Arrays.stream(req.getCookies())
						.filter(c -> c.getName().equals(COOKIE_NAME_PREFIX + validCookieValue))
						.findFirst()
						.orElse(null);

		// 쿠키 이름 & 값 일치하는지 확인
		boolean doesCookieExist = cookie != null && cookie.getValue().equals(validCookieValue);

		// 쿠키 없으면 좋아요 증가, 있으면 감소
		Function<Long, ReviewCommentEntity> adjustLike = doesCookieExist ?
				reviewCommentService::decreaseCommentLike
				: reviewCommentService::increaseCommentLike;

		// 쿠키 없으면 새로 생성, 있으면 삭제
		BiConsumer<HttpServletResponse, String> handleCookie = doesCookieExist ?
				this::deleteCookie : this::saveCookie;

		// 댓글 좋아요 증감, 쿠키 처리 진행
		ReviewCommentEntity entity = adjustLike.apply(reviewCommentId);
		handleCookie.accept(resp, validCookieValue);

		String resultMessage = "댓글의 좋아요를 " + (doesCookieExist ? "감소" : "증가") + "시켰습니다.";
		resultMessage += " [" + entity.getLike() + "]";

		return ApiResponse.onSuccess(Message.createMessage(resultMessage));
	}

	private void saveCookie(HttpServletResponse resp, String validCookieValue) {
		Cookie cookie = new Cookie(COOKIE_NAME_PREFIX + validCookieValue, validCookieValue);

		int year = 365 * 24 * 60 * 60;
		cookie.setMaxAge(year);
		cookie.setHttpOnly(true);

		resp.addCookie(cookie);
	}

	private void deleteCookie(HttpServletResponse resp, String validCookieValue) {
		Cookie cookie = new Cookie(COOKIE_NAME_PREFIX + validCookieValue, validCookieValue);
		cookie.setMaxAge(0);
		resp.addCookie(cookie);
	}
}
