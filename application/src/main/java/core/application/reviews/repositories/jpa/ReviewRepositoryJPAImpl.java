package core.application.reviews.repositories.jpa;

import core.application.reviews.models.entities.*;
import core.application.reviews.repositories.*;
import core.application.reviews.repositories.jpa.repositories.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Repository
@Profile("jpa")
@RequiredArgsConstructor
public class ReviewRepositoryJPAImpl implements
        ReviewRepository {

    private final JpaReviewRepository jpaRepo;

    @Override
    public ReviewEntity saveNewReview(String movieId, UUID userId, ReviewEntity review) {
        return null;
    }

    @Override
    public Optional<ReviewEntity> findByReviewId(Long reviewId) {
        return Optional.empty();
    }

    @Override
    public Optional<ReviewEntity> findByReviewIdWithoutContent(Long reviewId) {
        return Optional.empty();
    }

    @Override
    public List<ReviewEntity> findByMovieId(String movieId, int offset, int num) {
        return List.of();
    }

    @Override
    public List<ReviewEntity> findByMovieIdOnDateDescend(String movieId, int offset, int num) {
        return List.of();
    }

    @Override
    public List<ReviewEntity> findByMovieIdOnLikeDescend(String movieId, int offset, int num) {
        return List.of();
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContent(String movieId, int offset, int num) {
        return List.of();
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContentOnDateDescend(String movieId, int offset,
            int num) {
        return List.of();
    }

    @Override
    public List<ReviewEntity> findByMovieIdWithoutContentOnLikeDescend(String movieId, int offset,
            int num) {
        return List.of();
    }

    @Override
    public List<ReviewEntity> findByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<ReviewEntity> selectAll() {
        return List.of();
    }

    @Override
    public ReviewEntity editReviewInfo(Long reviewId, ReviewEntity replacement) {
        return null;
    }

    @Override
    public ReviewEntity updateReviewLikes(Long reviewId, int givenLikes) {
        return null;
    }

    @Override
    public void deleteReview(Long reviewId) {

    }
}
