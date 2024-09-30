package core.application.reviews.repositories;

import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.repositories.ReviewRepository;
import core.application.reviews.repositories.mapper.ReviewMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.json.XMLTokener.entity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {


    @Autowired
    private ReviewRepository reviewRepository;

    private static ReviewEntity testReview;


    private static final UUID userId = UUID.fromString("28a771d8-7f15-11ef-a312-2cf05d34871e");
    private static final Long reviewId = 2L;
    private static final String movieId = "Movie2";


    @Test
    void testSelectAll() {
        System.out.println("기본 정보 세팅");

        testReview = ReviewEntity.builder()
                .reviewId(reviewId)
                .title("Test Movie Review")
                .content("This is a test review")
                .userId(userId)
                .movieId(movieId)
                .like(100)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        System.out.println(testReview);


        reviewRepository.saveNewReview(movieId, userId, testReview);
        System.out.println("..");
        List<ReviewEntity> find = reviewRepository.findByMovieId(testReview.getMovieId());
        Assertions.assertThat(find).isEqualTo(entity);
    }
}