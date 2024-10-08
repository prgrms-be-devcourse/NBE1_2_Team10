package core.application.reviews.repositories;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.movie.CachedMovieRepository;
import core.application.reviews.models.entities.ReviewEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CachedMovieRepository movieRepo;

    private final Comparator<ReviewEntity> dateDescend
            = comparing(ReviewEntity::getCreatedAt).reversed()
            .thenComparing(comparing(ReviewEntity::getReviewId).reversed());
    private final Comparator<ReviewEntity> likeDescend
            = comparing(ReviewEntity::getLike).reversed()
            .thenComparing(comparing(ReviewEntity::getReviewId).reversed());


    // DB 에 존재하는 유저 id
    // Table DDL 에서 UUID 가 generate 되는게 아니라 DEFAULT EXPRESSION 으로 생성되서
    // MyBatis useGeneratedKey 인식을 못함
    // 그래서 나중에 saveNewUser 메서드 UUID 도 제공 받아서 저장되도록 만들어야됨 tq
    private static final UUID userId = UUID.fromString("01cbb144-851e-11ef-b878-00d861a152a7");
    private static UserEntity testUser;
    private static CachedMovieEntity testMovie;

    private static List<ReviewEntity> testReviews1;
    private static List<ReviewEntity> testReviews2;
    private static final int TEST_SIZE = 10;


    private static ReviewEntity genReview(UUID userId, String movieId, int like) {
        return ReviewEntity.builder()
                .title("Testing")
                .content("Testing")
                .like(like)
                .userId(userId)
                .movieId(movieId)
                .createdAt(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        testUser = UserEntity.builder()
                .userId(userId)
                .userEmail("review-repo-testing-@test.com")
                .userName("review-repo-testing")
                .role(UserRole.USER)
                .userPw("testing")
                .build();

        testMovie = CachedMovieEntity.builder()
                .movieId("review-repo-testing-movie")
                .title("review-repo-testing-movie")
                .posterUrl("review-repo-testing-movie")
                .dibCount(0L)
                .reviewCount(0L)
                .commentCount(0L)
                .sumOfRating(0L)
                .build();
    }

    @BeforeEach
    void setUp() {
        try {
            movieRepo.saveNewMovie(testMovie);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }

        testReviews1 = IntStream.range(0, TEST_SIZE).parallel()
                .boxed()
                .map(i -> genReview(testUser.getUserId(), testMovie.getMovieId(), TEST_SIZE - i))
                .toList();
    }

    @Test
    @DisplayName("새 리뷰 글을 만든다.")
    void saveNewReview() {
        ReviewEntity testReview = genReview(testUser.getUserId(), testMovie.getMovieId(), 0);

        ReviewEntity result = reviewRepo.saveNewReview(testReview.getMovieId(),
                testReview.getUserId(), testReview);

        assertThat(result.getReviewId()).isNotNull();
        assertThat(testReview.getReviewId()).isNotNull();

        log.info(result.toString());
    }

    @Test
    @DisplayName("리뷰글 ID 로 찾는다.")
    void findByReviewId() {
        ReviewEntity testReview = reviewRepo.saveNewReview(
                testMovie.getMovieId(), testUser.getUserId(),
                genReview(null, null, 0));

        ReviewEntity result = reviewRepo.findByReviewId(testReview.getReviewId()).orElseThrow();

        assertThat(testReview.equals(result)).isTrue();
    }

    @Test
    @DisplayName("특정 영화에 달린 리뷰글을 보여준다.")
    void findByMovieId() {
        List<ReviewEntity> testList = testReviews1.stream()
                .map(t -> reviewRepo.saveNewReview(t.getMovieId(), t.getUserId(), t))
                .sorted(Comparator.comparing(ReviewEntity::getReviewId).reversed())
                .toList();

        List<ReviewEntity> result1 = reviewRepo.findByMovieId(testMovie.getMovieId(), 0,
                TEST_SIZE / 2);

        testList.forEach(t -> log.info(t.toString()));

        assertThat(result1).hasSize(TEST_SIZE / 2);
        assertThat(testList).containsAll(result1);

        result1.forEach(t -> log.info(t.toString()));

        List<ReviewEntity> result2 = reviewRepo.findByMovieId(testMovie.getMovieId(), TEST_SIZE / 2,
                TEST_SIZE / 2);

        assertThat(result2).hasSize(TEST_SIZE / 2);
        assertThat(testList).containsAll(result2);

        assertThat(result1).doesNotContainAnyElementsOf(result2);
    }

    @Test
    @DisplayName("특정 영화에 달린 리뷰글을 최신순으로 보여준다.")
    void findByMovieIdOnDateDescend() {
        List<ReviewEntity> testList = testReviews1.stream()
                .map(t -> reviewRepo.saveNewReview(t.getMovieId(), t.getUserId(), t))
                .sorted(dateDescend)
                .toList();

        List<ReviewEntity> result1 = reviewRepo.findByMovieIdOnDateDescend(testMovie.getMovieId(),
                0, TEST_SIZE / 2);
        List<ReviewEntity> result2 = reviewRepo.findByMovieIdOnDateDescend(testMovie.getMovieId(),
                TEST_SIZE / 2, TEST_SIZE / 2);

        testList.forEach(t -> log.info(t.toString()));
        result1.forEach(t -> log.info(t.toString()));
        result2.forEach(t -> log.info(t.toString()));

        assertThat(result1).hasSize(TEST_SIZE / 2);
        assertThat(testList).containsAll(result1);
        assertThat(result1).isSortedAccordingTo(dateDescend);

        assertThat(result2).hasSize(TEST_SIZE / 2);
        assertThat(testList).containsAll(result2);
        assertThat(result2).isSortedAccordingTo(dateDescend);

        assertThat(result1).doesNotContainAnyElementsOf(result2);
    }

    @Test
    @DisplayName("특정 영화에 달린 리뷰글을 좋아요 순으로 보여준다.")
    void findByMovieIdOnLikeDescend() {
        List<ReviewEntity> testList = testReviews1.stream()
                .map(t -> reviewRepo.saveNewReview(t.getMovieId(), t.getUserId(), t))
                .sorted(likeDescend)
                .toList();

        List<ReviewEntity> result1 = reviewRepo.findByMovieIdOnLikeDescend(testMovie.getMovieId(),
                0, TEST_SIZE / 2);
        List<ReviewEntity> result2 = reviewRepo.findByMovieIdOnLikeDescend(testMovie.getMovieId(),
                TEST_SIZE / 2, TEST_SIZE / 2);

        testList.forEach(t -> log.info(t.toString()));
        result1.forEach(t -> log.info(t.toString()));
        result2.forEach(t -> log.info(t.toString()));

        assertThat(result1).hasSize(TEST_SIZE / 2);
        assertThat(testList).containsAll(result1);
        assertThat(result1).isSortedAccordingTo(likeDescend);

        assertThat(result2).hasSize(TEST_SIZE / 2);
        assertThat(testList).containsAll(result2);
        assertThat(result2).isSortedAccordingTo(likeDescend);

        assertThat(result1).doesNotContainAnyElementsOf(result2);
    }

    @Test
    @DisplayName("특정 영화에 달린 리뷰글을 본문을 제외하고 불러온다.")
    void findByMovieIdWithoutContent() {
        List<ReviewEntity> testList = testReviews1.stream()
                .map(t -> reviewRepo.saveNewReview(t.getMovieId(), t.getUserId(), t))
                .sorted(Comparator.comparing(ReviewEntity::getReviewId).reversed())
                .toList();

        List<ReviewEntity> result1 = reviewRepo.findByMovieIdWithoutContent(testMovie.getMovieId(),
                0, TEST_SIZE / 2);
        List<ReviewEntity> result2 = reviewRepo.findByMovieIdWithoutContent(testMovie.getMovieId(),
                TEST_SIZE / 2, TEST_SIZE / 2);

        testList.forEach(t -> log.info(t.toString()));
        result1.forEach(t -> log.info(t.toString()));
        result2.forEach(t -> log.info(t.toString()));

        assertThat(result1).hasSize(TEST_SIZE / 2);
        result1.forEach(r -> assertThat(r.getContent()).isNull());

        assertThat(result2).hasSize(TEST_SIZE / 2);
        result2.forEach(t -> assertThat(t.getContent()).isNull());
    }

    @Test
    @DisplayName("특정 리뷰 글을 수정한다.")
    void editReviewInfo() {
        ReviewEntity testReview = reviewRepo.saveNewReview(testMovie.getMovieId(),
                testUser.getUserId(), genReview(null, null, 0));

        ReviewEntity replacement = ReviewEntity.builder()
                .title("replacement")
                .content("replacement-content")
                .userId(UUID.randomUUID())
                .build();

        log.info(testReview.toString());
        log.info(replacement.toString());

        ReviewEntity result = reviewRepo.editReviewInfo(testReview.getReviewId(),
                replacement);

        assertThat(result).satisfies(
                r -> assertThat(r.getReviewId()).isEqualTo(testReview.getReviewId()),
                r -> assertThat(r.getUserId()).isEqualTo(testUser.getUserId()),
                r -> assertThat(r.getMovieId()).isEqualTo(testMovie.getMovieId()),
                r -> assertThat(r.getCreatedAt()).isEqualTo(testReview.getCreatedAt()),
                r -> assertThat(r.getTitle()).isEqualTo(replacement.getTitle()),
                r -> assertThat(r.getContent()).isEqualTo(replacement.getContent())
        );

        log.info(result.toString());
    }

    @Test
    @DisplayName("특정 리뷰 글을 삭제한다.")
    void deleteReview() {
        ReviewEntity testReview = reviewRepo.saveNewReview(testMovie.getMovieId(),
                testUser.getUserId(), genReview(null, null, 0));

        assertThat(reviewRepo.findByReviewId(testReview.getReviewId())).isNotEmpty();

        reviewRepo.deleteReview(testReview.getReviewId());

        assertThat(reviewRepo.findByReviewId(testReview.getReviewId())).isEmpty();
    }
}