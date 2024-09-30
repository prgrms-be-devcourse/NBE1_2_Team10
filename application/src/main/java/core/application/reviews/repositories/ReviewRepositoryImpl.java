package core.application.reviews.repositories;

import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.repositories.mapper.ReviewMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository{

    private final ReviewMapper reviewMapper;

    public ReviewRepositoryImpl(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewEntity saveNewReview(String movieId, UUID userId, ReviewEntity review) {
        reviewMapper.saveNewReview(movieId, userId, review);
        return review;
    }

    @Override
    public Optional<ReviewEntity> findByReviewId(Long reviewId) {
        return reviewMapper.findByReviewId(reviewId);
    }

    @Override
    public List<ReviewEntity> findByMovieId(String movieId) {
        return reviewMapper.findByMovieId(movieId);
    }

    @Override
    public List<ReviewEntity> findByMovieIdOnDateDescend(String movieId) {
        return reviewMapper.findByMovieIdOnDateDescend(movieId);
    }

    @Override
    public List<ReviewEntity> findByMovieIdOnLikeDescend(String movieId) {
        return reviewMapper.findByMovieIdOnLikeDescend(movieId);
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContent(String movieId) {
        return reviewMapper.findByMovieIdWithoutContent(movieId);
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContentOnDateDescend(String movieId) {
        return reviewMapper.findByMovieIdWithoutContentOnDateDescend(movieId);
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContentOnLikeDescend(String movieId) {
        return reviewMapper.findByMovieIdWithoutContentOnLikeDescend(movieId);
    }

    @Override
    public List<ReviewEntity> findByUserId(UUID userId) {
        return reviewMapper.findByUserId(userId);
    }

    @Override
    public List<ReviewEntity> selectAll() {
        return reviewMapper.selectAll();
    }

    @Override
    public ReviewEntity editReviewInfo(Long reviewId, ReviewEntity replacement) {
        reviewMapper.editReviewInfo(reviewId, replacement);
        return replacement;
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewMapper.deleteReview(reviewId);
    }
}
