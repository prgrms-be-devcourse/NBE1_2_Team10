package core.application.reviews.services;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import core.application.reviews.exceptions.NoReviewCommentFoundException;
import core.application.reviews.exceptions.NoReviewFoundException;
import core.application.reviews.models.entities.ReviewCommentEntity;
import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.repositories.ReviewCommentRepository;
import core.application.reviews.repositories.ReviewRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@Transactional
class ReviewCommentServiceImplTest {

    private final Logger log = Logger.getLogger(ReviewCommentServiceImplTest.class.getName());

    @MockBean
    private WebClient webClient;

    @Autowired
    private ReviewCommentService reviewCommentService;

    @MockBean
    private ReviewCommentRepository reviewCommentRepo;

    @MockBean
    private ReviewRepository reviewRepo;

    private static final Random random = new Random();
    private static final int testSize = 10;

    private static Comparator<ReviewCommentEntity> latestOrder = comparing(
            ReviewCommentEntity::getCreatedAt).reversed()
            .thenComparing(comparing(ReviewCommentEntity::getReviewCommentId).reversed());
    private static Comparator<ReviewCommentEntity> mostLikeOrder = comparing(
            ReviewCommentEntity::getLike)
            .thenComparing(comparing(ReviewCommentEntity::getReviewCommentId));


    private ReviewCommentEntity genComment(Long reviewCommentId, Long reviewId, UUID userId,
            Long groupId, Long commentRef, Instant createdAt, boolean isUpdated) {
        return ReviewCommentEntity.builder()
                .reviewCommentId(reviewCommentId)
                .reviewId(reviewId)
                .userId(userId)
                .groupId(groupId)
                .commentRef(commentRef)
                .createdAt(createdAt)
                .isUpdated(isUpdated)
                .build();
    }

    private List<ReviewCommentEntity> genComments(Long reviewId, Long groupId, Long commentRef) {
        return LongStream.range(0, testSize)
                .boxed()
                .map(i -> genComment(i, reviewId, UUID.randomUUID(),
                        groupId, commentRef, Instant.now(), false))
                .toList();
    }

    private List<ReviewCommentEntity> genParentComments(Long reviewId) {
        return genComments(reviewId, null, null);
    }

    private List<ReviewCommentEntity> genChildComments(Long reviewId, Long groupId) {
        return genComments(reviewId, groupId, null);
    }

    private void setupRepo(Long reviewId, Long reviewCommentId) {
        when(reviewRepo.findByReviewId(reviewId))
                .thenReturn(Optional.of(ReviewEntity.builder().build()));

        when(reviewCommentRepo.findByReviewCommentId(reviewCommentId))
                .thenReturn(Optional.of(ReviewCommentEntity.builder().build()));

        when(reviewRepo.findByReviewId(
                AdditionalMatchers.not(AdditionalMatchers.geq(reviewId))))
                .thenReturn(Optional.empty());

        when(reviewCommentRepo.findByReviewCommentId(
                AdditionalMatchers.not(AdditionalMatchers.geq(reviewCommentId))))
                .thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("특정 리뷰 포스팅의 부모 댓글을 불러오는 서비스")
    void getParentReviewComments() {
        // when
        log.info("<-- getParentReviewComments");

        Long reviewId = random.nextLong();
        ReviewEntity post = ReviewEntity.builder().build();

        List<ReviewCommentEntity> parentComments = genParentComments(reviewId);

        parentComments.forEach(
                p -> when(reviewRepo.findByReviewId(p.getReviewId()))
                        .thenReturn(Optional.of(post)));

        // org.mockito.AdditionalMatchers.*;
        when(reviewRepo.findByReviewId(AdditionalMatchers.not(AdditionalMatchers.geq(reviewId))))
                .thenReturn(Optional.empty());

        parentComments.forEach(
                p -> when(reviewCommentRepo.findParentCommentByReviewIdOnDateDescend(reviewId))
                        .thenReturn(parentComments.stream().sorted(latestOrder).toList())
        );
        parentComments.forEach(
                p -> when(reviewCommentRepo.findParentCommentByReviewIdOnLikeDescend(reviewId))
                        .thenReturn(parentComments.stream().sorted(mostLikeOrder).toList())
        );

        // given
        List<ReviewCommentEntity> onLatest = reviewCommentService.getParentReviewComments(reviewId,
                ReviewCommentSortOrder.LATEST);
        List<ReviewCommentEntity> onLikes = reviewCommentService.getParentReviewComments(reviewId,
                ReviewCommentSortOrder.LIKE);

        // then
        assertThat(onLatest).containsAll(parentComments);
        assertThat(onLikes).containsAll(parentComments);

        assertThat(onLatest).isSortedAccordingTo(latestOrder);
        assertThat(onLikes).isSortedAccordingTo(mostLikeOrder);

        assertThatThrownBy(() -> {
            reviewCommentService.getParentReviewComments(random.nextLong(),
                    ReviewCommentSortOrder.LATEST);
        }).isInstanceOf(NoReviewFoundException.class);

        assertThatThrownBy(() -> {
            reviewCommentService.getParentReviewComments(random.nextLong(),
                    ReviewCommentSortOrder.LIKE);
        }).isInstanceOf(NoReviewFoundException.class);

        log.info("--> getParentReviewComments test passed");
    }

    @Test
    @DisplayName("특정 포스팅에 부모 댓글 다는 서비스")
    void addNewParentReviewComment() {
        log.info("<-- addNewParentReviewComment");

        Long reviewId = random.nextLong();
        ReviewCommentEntity temp = ReviewCommentEntity.builder().build();

        setupRepo(reviewId, random.nextLong());

        assertThatThrownBy(() -> {
            reviewCommentService.addNewParentReviewComment(random.nextLong(), UUID.randomUUID(),
                    temp);
        }).isInstanceOf(NoReviewFoundException.class);

        reviewCommentService.addNewParentReviewComment(reviewId, UUID.randomUUID(), temp);

        log.info("--> addNewParentReviewComment test passed");
    }

    @Test
    @DisplayName("특정 포스팅 내 부모 댓글에 자식 댓글 다는 서비스")
    void addNewChildReviewComment() {
        log.info("<-- addNewChildReviewComment");

        Long reviewId = random.nextLong();
        Long groupId = random.nextLong();

        ReviewCommentEntity temp = ReviewCommentEntity.builder().build();

        setupRepo(reviewId, groupId);

        assertThatThrownBy(() -> {
            reviewCommentService.addNewChildReviewComment(random.nextLong(), groupId,
                    UUID.randomUUID(), temp);
        }).isInstanceOf(NoReviewFoundException.class);

        assertThatThrownBy(() -> {
            reviewCommentService.addNewChildReviewComment(reviewId, random.nextLong(),
                    UUID.randomUUID(), temp);
        }).isInstanceOf(NoReviewCommentFoundException.class);

        reviewCommentService.addNewChildReviewComment(reviewId, groupId, UUID.randomUUID(), temp);

        log.info("--> addNewChildReviewComment test passed");
    }

    @Test
    @DisplayName("특정 댓글의 내용을 수정하는 서비스")
    void editReviewComment() {
        log.info("<-- editReviewComment");

        Long reviewCommentId = random.nextLong();
        Long refId = random.nextLong();

        String replacement = "this is replacement";

        setupRepo(random.nextLong(), reviewCommentId);
        setupRepo(random.nextLong(), refId);

        assertThatThrownBy(() -> {
            reviewCommentService.editReviewComment(random.nextLong(), null, replacement);
        }).isInstanceOf(NoReviewCommentFoundException.class);

        assertThatThrownBy(() -> {
            reviewCommentService.editReviewComment(reviewCommentId, random.nextLong(), replacement);
        }).isInstanceOf(NoReviewCommentFoundException.class);

        when(reviewCommentRepo.findByReviewCommentId(reviewCommentId)).thenReturn(
                Optional.of(ReviewCommentEntity.builder().build()));

        reviewCommentService.editReviewComment(reviewCommentId, refId, replacement);

        log.info("--> editReviewComment test passed");
    }

    @Test
    @DisplayName("특정 댓글을 삭제하는 서비스")
    void deleteReviewComment() {
        log.info("<-- deleteReviewComment");

        Long reviewCommentId = random.nextLong();
        setupRepo(random.nextLong(), reviewCommentId);

        assertThatThrownBy(() -> {
            reviewCommentService.deleteReviewComment(random.nextLong());
        }).isInstanceOf(NoReviewCommentFoundException.class);

        reviewCommentService.deleteReviewComment(reviewCommentId);

        log.info("--> deleteReviewComment test passed");
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 증가시키는 서비스")
    void increaseCommentLike() {
        log.info("<-- increaseCommentLike");

        Long reviewCommentId = random.nextLong();

        setupRepo(random.nextLong(), reviewCommentId);

        assertThatThrownBy(() -> {
            reviewCommentService.increaseCommentLike(random.nextLong());
        }).isInstanceOf(NoReviewCommentFoundException.class);

        reviewCommentService.increaseCommentLike(reviewCommentId);

        log.info("--> increaseCommentLike test passed");
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 감소시키는 서비스")
    void decreaseCommentLike() {
        log.info("<-- decreaseCommentLike");

        Long reviewCommentId = random.nextLong();

        setupRepo(random.nextLong(), reviewCommentId);

        assertThatThrownBy(() -> {
            reviewCommentService.decreaseCommentLike(random.nextLong());
        }).isInstanceOf(NoReviewCommentFoundException.class);

        reviewCommentService.decreaseCommentLike(reviewCommentId);

        log.info("--> decreaseCommentLike test passed");
    }
}