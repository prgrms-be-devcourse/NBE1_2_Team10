package core.application.reviews.controllers;


import core.application.reviews.exceptions.NoReviewFoundException;
import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.services.ReviewService;
import core.application.reviews.services.ReviewSortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/list")
    public String listReviews(@PathVariable Long reviewId,
                              Model model) {
        model.addAttribute("reviews", reviewService.loadReview(reviewId, ReviewSortOrder.LATEST, true));
        return "reviews/reviewList";
    }

    @PostMapping
    public String createReview(@PathVariable String movieId,
                               @RequestParam("userId") UUID userId,
                               @ModelAttribute ReviewEntity reviewEntity) {
        reviewService.createReview(movieId, userId, reviewEntity);
        return "redirect:/movies/" + movieId + "/reviews/list";  // 작성 후 리뷰 목록 페이지로 이동
    }

    /**
     *  리뷰 상세 조회
     * @param movieId 영화 ID
     * @param reviewId 리뷰 ID
     * @param withContent 본문 여부
     * @param model
     * @return
     */
    @GetMapping("/{reviewId}")
    public String getReview(@PathVariable String movieId,
                            @PathVariable Long reviewId,
                            @RequestParam(defaultValue = "true") boolean withContent,
                            Model model) {
        try {
            Optional<ReviewEntity> review = reviewService.loadReview(reviewId, ReviewSortOrder.LATEST, withContent);
            model.addAttribute("review", review.orElseThrow(() -> new NoReviewFoundException("리뷰를 찾을 수 없습니다.")));
            return "reviews/{reviewId}";
        } catch (NoReviewFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     *  리뷰 수정
     * @param movieId 영화 ID
     * @param reviewId 리뷰 ID
     * @param model
     * @return
     */
    @GetMapping("/{reviewId}/edit")
    public String editReview(@PathVariable String movieId,
                                     @PathVariable Long reviewId,
                                     Model model) {
        try {
            Optional<ReviewEntity> review = reviewService.loadReview(reviewId, ReviewSortOrder.LATEST, true);
            model.addAttribute("review", review.orElseThrow(() -> new NoReviewFoundException("리뷰를 찾을 수 없습니다.")));
            return "reviews/reviewEdit";
        } catch (NoReviewFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * 리뷰 삭제
     * @param movieId 영화 ID
     * @param reviewId 리뷰 ID
     * @return 리뷰 삭제 후 목록으로
     */
    @PostMapping("/{reviewId}/delete")
    public String deleteReview(@PathVariable String movieId,
                               @PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return "redirect:/movies/" + movieId + "/reviews/list";  // 삭제 후 목록 페이지로 이동
        } catch (NoReviewFoundException e) {
            return "redirect:/error?message=" + e.getMessage();
        }
    }
}
