package core.application.reviews.services;

import core.application.reviews.exceptions.NoReviewFoundException;
import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public ReviewEntity createReview(String movieId, UUID userId, ReviewEntity reviewEntity) {
        ReviewEntity newReview = ReviewEntity.builder()
                .movieId(movieId)
                .userId(userId)
                .title(reviewEntity.getTitle())
                .content(reviewEntity.getContent())
                .like(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // 데이터베이스에 저장
        reviewRepository.saveNewReview(movieId, userId, newReview);
        return newReview;
    }

    // 리뷰 조회
    @Override
    public Optional<ReviewEntity> loadReview(Long reviewId, ReviewSortOrder order, boolean withContent) throws NoReviewFoundException {
        Optional<ReviewEntity> review = reviewRepository.findByReviewId(reviewId);

        if (!review.isPresent()){
            throw new NoReviewFoundException("리뷰 ID" + reviewId + "에 해당하는 리뷰가 없습니다");
        }

        if(!withContent){
            throw new NoReviewFoundException("내용을 작성해주세요");
        }

        return review;
    }

    // 리뷰 업데이트
    @Override
    public ReviewEntity updateReview(Long reviewId, ReviewEntity updateReview) throws NoReviewFoundException {
        ReviewEntity existingReview = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new NoReviewFoundException("리뷰 ID" + reviewId + "에 해당하는 리뷰가 없습니다"));

        ReviewEntity updatedReview = existingReview.withTitle(updateReview.getTitle())
                .withContent(updateReview.getContent())
                .withUpdatedAt(Instant.now());

        return reviewRepository.saveNewReview(updateReview.getMovieId(), updatedReview.getUserId(), updatedReview);
    }

    // 리뷰 삭제
    @Override
    public ReviewEntity deleteReview(Long reviewId) throws NoReviewFoundException {
        ReviewEntity existingReview = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new NoReviewFoundException("리뷰 ID" + reviewId + "에 해당하는 리뷰가 없습니다"));

        reviewRepository.deleteReview(reviewId);
        return existingReview;
    }

    // 좋아요 추가
    @Override
    public ReviewEntity likeCount(Long reviewId, UUID userId) throws NoReviewFoundException {
        ReviewEntity review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new NoReviewFoundException("리뷰 ID " + reviewId + " 에 해당하는 리뷰를 찾을 수 없습니다."));

        if (!review.getLikeUsers().contains(userId)) {
            review = review.increaseLikes(userId);
        }

        return reviewRepository.saveNewReview(review.getMovieId(), review.getUserId(), review);
    }

    // 좋아요 취소
    @Override
    public ReviewEntity likeCancel(Long reviewId, UUID userId) throws NoReviewFoundException {
        ReviewEntity review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new NoReviewFoundException("리뷰 ID " + reviewId + " 에 해당하는 리뷰를 찾을 수 없습니다."));

        if(review.getLikeUsers().contains(userId)){
            review = review.decreaseLikes(userId);
        }
        return reviewRepository.saveNewReview(review.getMovieId(), review.getUserId(), review);
    }
}
