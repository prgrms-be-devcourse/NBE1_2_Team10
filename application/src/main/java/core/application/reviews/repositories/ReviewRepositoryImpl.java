package core.application.reviews.repositories;

import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.repositories.mapper.ReviewMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements
        ReviewRepository {

    private final ReviewMapper mapper;

    @Override
    public ReviewEntity saveNewReview(String movieId, UUID userId, ReviewEntity review) {
        review.setCreatedAt(Instant.now());
        mapper.saveNewReview(movieId, userId, review);
        return mapper.findByReviewId(review.getReviewId()).orElseThrow();
    }

    @Override
    public Optional<ReviewEntity> findByReviewId(Long reviewId) {
        return mapper.findByReviewId(reviewId);
    }

    @Override
    public List<ReviewEntity> findByMovieId(String movieId, int offset, int num) {
        return mapper.findByMovieId(movieId, offset, num);
    }

    @Override
    public List<ReviewEntity> findByMovieIdOnDateDescend(String movieId, int offset, int num) {
        return mapper.findByMovieIdOnDateDescend(movieId, offset, num);
    }

    @Override
    public List<ReviewEntity> findByMovieIdOnLikeDescend(String movieId, int offset, int num) {
        return mapper.findByMovieIdOnLikeDescend(movieId, offset, num);
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContent(String movieId, int offset, int num) {
        return mapper.findByMovieIdWithoutContent(movieId, offset, num);
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContentOnDateDescend(
            String movieId, int offset, int num) {
        return mapper.findByMovieIdWithoutContentOnDateDescend(movieId, offset, num);
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContentOnLikeDescend(
            String movieId, int offset, int num) {
        return mapper.findByMovieIdWithoutContentOnLikeDescend(movieId, offset, num);
    }

    @Override
    public List<ReviewEntity> findByUserId(UUID userId) {
        return mapper.findByUserId(userId);
    }

    @Override
    public List<ReviewEntity> selectAll() {
        return mapper.selectAll();
    }


    @Override
    public ReviewEntity editReviewInfo(Long reviewId, ReviewEntity replacement) {
        replacement.setUpdatedAt(Instant.now());
        mapper.editReviewInfo(reviewId, replacement);
        return mapper.findByReviewId(reviewId).orElseThrow();
    }

    @Override
    public void deleteReview(Long reviewId) {
        mapper.deleteReview(reviewId);
    }
}
