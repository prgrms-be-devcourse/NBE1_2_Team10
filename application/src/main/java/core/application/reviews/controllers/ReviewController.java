package core.application.reviews.controllers;

import core.application.api.response.ApiResponse;
import core.application.api.response.code.Message;
import core.application.reviews.exceptions.InvalidPageException;
import core.application.reviews.exceptions.InvalidReviewEditException;
import core.application.reviews.exceptions.InvalidReviewWriteException;
import core.application.reviews.exceptions.NotReviewOwnerException;
import core.application.reviews.models.dto.request.reviews.CreateReviewReqDTO;
import core.application.reviews.models.dto.request.reviews.EditReviewReqDTO;
import core.application.reviews.models.dto.response.reviews.AdjustLikeRespDTO;
import core.application.reviews.models.dto.response.reviews.ListReviewsRespDTO;
import core.application.reviews.models.dto.response.reviews.ReviewInfoRespDTO;
import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.services.ReviewService;
import core.application.reviews.services.ReviewSortOrder;
import core.application.security.CustomUserDetails;
import core.application.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/movies/{movieId}/reviews")
@Tag(name = "Review", description = "Review 관련 API")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    private static final int REVIEWS_PER_PAGE = 10;
    private static final String COOKIE_NAME_PREFIX = "ReviewLikeAdjustment";

    /**
     * 특정 영화에 달린 포스팅을 보여주는 엔드포인트
     *
     * @param movieId 영화 ID
     * @param page    페이징 넘버
     * @param sort    정렬 순서 {@code (latest | like)}
     * @param content 본문을 포함해 응답할지 {@code Y/N}
     * @return 응답용 포스팅 목록들
     * @see ReviewSortOrder
     */
    @Operation(summary = "리뷰 목록 조회")
    @GetMapping("/list")
    public ApiResponse<ListReviewsRespDTO> listReviews(
            @PathVariable String movieId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
            @RequestParam(value = "content", defaultValue = "true") boolean content
    ) {

        if (page < 0) {
            throw new InvalidPageException("잘못된 리뷰 페이지입니다.");
        }

        int offset = page * REVIEWS_PER_PAGE;

        ReviewSortOrder order = Arrays.stream(ReviewSortOrder.values())
                .anyMatch(r -> r.name().equalsIgnoreCase(sort)) ?
                ReviewSortOrder.valueOf(sort.toUpperCase()) : ReviewSortOrder.LATEST;

        List<ReviewEntity> searchResult = reviewService.getReviewsOnMovieId(movieId, order, content,
                offset, REVIEWS_PER_PAGE);

        return ApiResponse.onSuccess(ListReviewsRespDTO.of(searchResult));
    }

    /**
     * 특정 포스팅의 정보를 보여주는 앤드포인트
     *
     * @param reviewId 포스팅 ID
     * @return 포스팅 정보를 담은 DTO
     */
    @Operation(summary = "특정 리뷰 조회")
    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewInfoRespDTO> getReviewInfo(@PathVariable("reviewId") Long reviewId) {

        ReviewEntity searchResult = reviewService.getReviewInfo(reviewId, true);

        // TODO 주어진 userId 에 해당하는 사용자가 없으면 던지는 exception 이 없네...?
        // TODO 나중에 생기면 orElseThrow 에 추가
        String userAlias = userService.getUserByUserId(searchResult.getUserId())
                .orElseThrow()
                .getAlias();

        return ApiResponse.onSuccess(ReviewInfoRespDTO.valueOf(userAlias, searchResult));
    }

    /**
     * 새로운 포스팅을 생성하는 엔드포인트
     *
     * @param movieId     포스팅할 영화 ID
     * @param userDetails {@code Security context holder} 에 존재하는 유저 {@code principal}
     * @param reqDTO      포스팅 생성 요청 {@code DTO}
     */
    @Operation(summary = "리뷰 작성")
    @PostMapping
    public ApiResponse<Message> createReview(
            @PathVariable("movieId") String movieId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Validated CreateReviewReqDTO reqDTO,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            throw new InvalidReviewWriteException(
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Spring context holder 에서 CustomUserDetails 가져오고
        // CustomUserDetails 에서 userId 꺼냄
        UUID userId = userDetails.getUserId();

        // 해당하는 영화가 DB 에 없으면 createNewReview 에서 throw
        ReviewEntity result = reviewService.createNewReview(movieId, userId,
                reqDTO.getTitle().trim(),
                reqDTO.getContent());

        return ApiResponse.onCreateSuccess(Message.createMessage("성공적으로 글을 작성하였습니다."));
    }

    /**
     * 특정 포스팅을 수정하는 엔드포인트
     *
     * @param reviewId    수정할 포스팅 ID
     * @param userDetails {@code Security context holder} 에 존재하는 유저 {@code principal}
     * @param reqDTO      수정 요청 {@code DTO}
     */
    @Operation(summary = "리뷰 수정")
    @PatchMapping("/{reviewId}")
    public ApiResponse<Message> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Validated EditReviewReqDTO reqDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InvalidReviewEditException(
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Spring context holder 에서 CustomUserDetails 가져오고
        // CustomUserDetails 에서 userId 꺼냄
        UUID userId = userDetails.getUserId();

        // reviewId 해당 포스팅 없으면 getReviewInfo 에서 throw
        ReviewEntity origin = reviewService.getReviewInfo(reviewId, false);

        // 작성자 확인
        if (!userId.equals(origin.getUserId())) {
            throw new NotReviewOwnerException("글 작성자만 수정할 수 있습니다.");
        }

        ReviewEntity replacement = ReviewEntity.builder()
                .title(reqDTO.getTitle().trim())
                .content(reqDTO.getContent())
                .build();

        ReviewEntity result = reviewService.updateReviewInfo(reviewId, replacement);

        return ApiResponse.onSuccess(Message.createMessage("성공적으로 글을 수정하였습니다."));
    }

    /**
     * 특정 포스팅을 삭제하는 엔드포인트
     *
     * @param reviewId    삭제할 포스팅 ID
     * @param userDetails {@code Security context holder} 에 존재하는 유저 {@code principal}
     */
    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Message> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Spring context holder 에서 CustomUserDetails 가져오고
        // CustomUserDetails 에서 userId 꺼냄
        UUID userId = userDetails.getUserId();

        // reviewId 해당 포스팅 없으면 getReviewInfo 에서 throw
        ReviewEntity origin = reviewService.getReviewInfo(reviewId, false);

        if (!userId.equals(origin.getUserId())) {
            throw new NotReviewOwnerException("글 작성자만 삭제할 수 있습니다.");
        }

        ReviewEntity result = reviewService.deleteReview(reviewId);

        return ApiResponse.onSuccess(Message.createMessage("성공적으로 글을 삭제하였습니다."));
    }

    /**
     * 포스팅의 좋아요를 증감시키는 엔드포인트 {@code (쿠키 이용)}
     *
     * @param reviewId    포스팅 ID
     * @param userDetails {@code userEmail} 을 가져오기 위한 {@code principal}
     * @param req         쿠키 가져오기 위한 {@code request}
     * @param resp        쿠키 저장하기 위한 {@code response}
     */
    @Operation(summary = "리뷰 좋아요")
    @PatchMapping("/{reviewId}/like")
    public ApiResponse<AdjustLikeRespDTO> adjustLike(
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest req, HttpServletResponse resp) {

        // hash 값 이용해서 쿠키 이름, 값 증빙할거임
        String validCookieValue = String.valueOf(
                Objects.hash(reviewId, userDetails.getUserEmail()));

        // request 내 쿠키 중 이름 일치하는 쿠키 확인
        Cookie cookie = req.getCookies() == null ? null :
                Arrays.stream(req.getCookies())
                        .filter(c -> c.getName().equals(COOKIE_NAME_PREFIX + validCookieValue))
                        .findFirst()
                        .orElse(null);

        // 쿠키 이름 & 값 일치하는지 확인
        boolean doesCookieExist = cookie != null && cookie.getValue().equals(validCookieValue);

        // 쿠키 없으면 좋아요 증가, 있으면 감소
        Function<Long, ReviewEntity> adjustLike = doesCookieExist ?
                reviewService::decreaseLikes : reviewService::increaseLikes;

        // 쿠키 없으면 쿠키 새로 생성, 있으면 삭제
        BiConsumer<HttpServletResponse, String> handleCookie = doesCookieExist ?
                this::deleteCookie : this::saveCookie;

        // 포스팅 좋아요 증감, 쿠키 처리 진행
        ReviewEntity entity = adjustLike.apply(reviewId);
        handleCookie.accept(resp, validCookieValue);

        String resultMessage = "리뷰의 좋아요를 " + (doesCookieExist ? "감소" : "증가") + "시켰습니다.";
        resultMessage += " [" + entity.getLike() + "]";

        AdjustLikeRespDTO adjustLikeRespDTO = new AdjustLikeRespDTO(resultMessage);
        return ApiResponse.onSuccess(adjustLikeRespDTO);
    }

    private void saveCookie(HttpServletResponse resp, String validCookieValue) {
        Cookie cookie = new Cookie(COOKIE_NAME_PREFIX + validCookieValue,
                validCookieValue);

        int year = 365 * 24 * 60 * 60;
        cookie.setMaxAge(year);
        cookie.setHttpOnly(true);

        resp.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse resp, String validCookieValue) {
        Cookie cookie = new Cookie(COOKIE_NAME_PREFIX + validCookieValue,
                validCookieValue);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }
}
