package core.application.reviews.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import core.application.reviews.models.entities.ReviewCommentEntity;
import core.application.reviews.models.entities.ReviewEntity;
import core.application.users.models.entities.UserEntity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewCommentRepositoryTest {

    private static final Logger log = Logger.getLogger(ReviewCommentRepositoryTest.class.getName());

    @Autowired
    private ReviewCommentRepository reviewCommentRepo;

    // DB 에 있는 실제 리뷰 포스팅의 ID
    private static final Long reviewId = 2L;

    // DB 에 있는 실제 유저의 ID
    private static final UUID userId = UUID.fromString("c2127ac8-7d74-11ef-a420-027a5d64df30");

    private static final int testSampleNum = 10;

    private static UserEntity testUser;
    private static ReviewEntity testReview;

    @BeforeAll
    static void init() {
        log.info("Initiating required test entities");

        testUser = UserEntity.builder()
                .userEmail("test@test.com")
                .userPw("test")
                .userName("test")
                .userId(userId)
                .build();

        testReview = ReviewEntity.builder().reviewId(reviewId).userId(testUser.getUserId()).build();
    }

    private final String testCommentContent = "this is temp content";

    private ReviewCommentEntity genTestEntity() {
        return genTestEntity(null, null, 0);
    }

    private ReviewCommentEntity genTestEntity(Long groupId, Long commentRef, int like) {
        return ReviewCommentEntity.builder()
                .reviewId(testReview.getReviewId())
                .userId(testUser.getUserId())
                .content(testCommentContent)
                .groupId(groupId)
                .like(like)
                .commentRef(commentRef)
                .createdAt(Instant.now()                // Instant 가 DB DATETIME 포맷보다 더 정밀함.
                        .truncatedTo(
                                ChronoUnit.SECONDS))   // MySQL DATETIME 은 초 단위까지여서 그 단위만큼 truncate
                .build();
    }

    // --------------------------------------------------------- //

    private final Long testGroupId = 12345L;
    private final Long testCommentId = 123456L;
    private final int testLikes = 333;

    /**
     * pk not null 아닌지 확인
     * <p>
     * userId, reviewId, 댓글 내용 일치 확인
     * <p>
     * 생성 날짜 not null 확인
     */
    private void checkNonNullValidation(ReviewCommentEntity result) {
        log.fine("<- checkNonNullValidation");

        // 반환 객체 세부 값들 확인
        assertThat(result).satisfies(
                t -> assertThat(result).isNotNull(),
                // pk 가 null 아닌지 확인
                t -> assertThat(result.getReviewCommentId()).isNotNull(),

                // userId 일치 확인
                t -> assertThat(result.getUserId()).isNotNull().isEqualTo(userId),

                // reviewId 일치 확인
                t -> assertThat(result.getReviewId()).isNotNull().isEqualTo(reviewId),

                // 댓글 내용 not null 확인
                t -> assertThat(result.getContent()).isNotNull(),

                // 생성 날짜 not null 확인
                t -> assertThat(result.getCreatedAt()).isNotNull());

        // 반환 된 객체 정상
        log.fine("Result validation passed.");
        log.fine("Review comment ID : " + result.getReviewCommentId());
        log.fine("<----->");
    }

    /**
     * groupId, commentRef, like 일치 확인
     */
    private void checkEquality(ReviewCommentEntity result, Long groupId, Long commentRef,
            int like) {
        log.fine("<- checkEquality");

        assertThat(result).satisfies(
                r -> assertThat(r.getGroupId()).isEqualTo(groupId),
                r -> assertThat(r.getCommentRef()).isEqualTo(commentRef),
                r -> assertThat(r.getLike()).isEqualTo(like));

        log.fine("Result equality validation passed.");
        log.fine("groupId : " + result.getGroupId());
        log.fine("commentRef : " + result.getCommentRef());
        log.fine("likes : " + result.getLike());
        log.fine("--> checkEquality end");
    }

    // --------------------------------------------------------- //


    @Test
    @DisplayName("주어진 내용 그대로 새로운 댓글 생성")
    void saveNewReviewComment() {
        log.info("<- saveNewReviewComment");

        ReviewCommentEntity testEntity = genTestEntity(testGroupId, testCommentId, testLikes);

        log.info("Saving new review comment");
        ReviewCommentEntity result = reviewCommentRepo.saveNewReviewComment(testEntity);

        // result 의 pk, userId, 댓글 내용, reviewId, 생성일 확인
        checkNonNullValidation(result);

        // result 의 groupId, commentRef, like 확인
        // testEntity 에 주어진 내용 그대로인지 확인
        checkEquality(result, testEntity.getGroupId(), testEntity.getCommentRef(),
                testEntity.getLike());

        log.info(
                "-> saveNewReviewComment test passed");
    }

    @Test
    @DisplayName("새로운 부모 댓글 생성")
    void saveNewParentReviewComment() {
        log.info(
                "<- saveNewParentReviewComment");

        ReviewCommentEntity testEntity = genTestEntity(testGroupId, testCommentId, testLikes);

        log.info("Saving new parent review comment");
        ReviewCommentEntity result = reviewCommentRepo.saveNewParentReviewComment(reviewId, userId,
                testEntity);

        // result 의 pk, userId, 댓글 내용, reviewId, 생성일 확인
        checkNonNullValidation(result);

        // result 의 groupId, commentRef, like 확인
        // 부모 댓글의 groupId 는 null, 댓글 생성시 like 는 0
        checkEquality(result, null, testEntity.getCommentRef(), 0);

        log.info("ResultEntity : " + result);
        log.info(
                "-> saveNewParentReviewComment test passed");
    }

    @Test
    @DisplayName("새로운 자식 댓글 생성")
    void saveNewChildReviewComment() {
        log.info("<- saveNewChildReviewComment");

        // invalid groupId were given
        ReviewCommentEntity testEntity = genTestEntity(883828L, testCommentId, testLikes);

        log.info("Saving new child review comment");
        ReviewCommentEntity result = reviewCommentRepo.saveNewChildReviewComment(testGroupId,
                userId, testEntity);

        // result 의 pk, userId, 댓글 내용, reviewId, 생성일 확인
        checkNonNullValidation(result);

        // result 의 groupId, commentRef, like 확인
        // 자식 댓글의 groupId 는 parent Id, 댓글 생성시 like 는 0
        // testEntity 의 groupId 가 invalid 해도 주어진 걸로 제대로 되는지 확인
        checkEquality(result, testGroupId, testEntity.getCommentRef(), 0);

        log.info("ResultEntity : " + result);
        log.info(
                "-> saveNewChildReviewComment test passed");
    }

    @Test
    @DisplayName("댓글 ID 로 검색")
    void findByReviewCommentId() {
        log.info("<- findByReviewCommentId");

        // DB 에 아무 정보 저장
        ReviewCommentEntity testEntity = reviewCommentRepo.saveNewReviewComment(genTestEntity());

        // 못찾으면 throw
        ReviewCommentEntity searchResult = reviewCommentRepo.findByReviewCommentId(
                testEntity.getReviewCommentId()).orElseThrow();

        // 다양한 non null 확인
        checkNonNullValidation(searchResult);

        // DB 에 넣었던 정보랑 일치하는지 확인
        assertThat(searchResult).isEqualTo(testEntity);

        log.info(
                "-> findByReviewCommentId test passed");
    }

    /**
     * 테스트 용 데이터 db 에 넣고 받기
     */
    private List<ReviewCommentEntity> insertTestEntities(Long groupId, Long commentRef,
            Comparator<ReviewCommentEntity> sortOrder) {
        return IntStream.range(0, testSampleNum)
                .boxed()
                .map(i -> genTestEntity(groupId, commentRef, 15 - i))
                .map(reviewCommentRepo::saveNewReviewComment)
                .sorted(sortOrder)
                .toList();
    }

    private List<ReviewCommentEntity> insertTestEntities(
            Comparator<ReviewCommentEntity> sortOrder) {
        return insertTestEntities(null, null, sortOrder);
    }

    @Test
    @DisplayName("어느 영화 리뷰에 달린 모든 부모 댓글을 검색")
    void findParentCommentByReviewId() {
        log.info(
                "<- findParentCommentByReviewId");

        // 확인 용 데이터 insert
        log.info("Saving new parent review comments");

        // reviewCommentId 순으로 정렬해 비교할 꺼임
        List<ReviewCommentEntity> testEntities = insertTestEntities(
                Comparator.comparing(ReviewCommentEntity::getReviewCommentId));

        // 검색 결과 reviewCommentId 순으로 정렬해 받음.
        List<ReviewCommentEntity> searchResult = reviewCommentRepo.findParentCommentByReviewId(
                        reviewId)
                .stream()
                .sorted(Comparator.comparing(ReviewCommentEntity::getReviewCommentId))
                .toList();

        // 모든 검색 결과 non null 확인
        searchResult.forEach(this::checkNonNullValidation);

        // 모두 부모 댓글인지 확인
        searchResult.forEach(r -> assertThat(r.getGroupId()).isNull());

        // 검색 결과에 넣었던 데이터 모두 존재하는지 확인
        assertThat(searchResult).containsAll(testEntities);

        log.info(
                "-> findParentCommentByReviewId test passed");
    }

    private final Comparator<ReviewCommentEntity> latestAndIdDescending = Comparator.comparing(
                    ReviewCommentEntity::getCreatedAt).reversed()   // 날짜 최신순
            .thenComparing(Comparator.comparing(ReviewCommentEntity::getReviewCommentId)
                    .reversed());   // id 내림차순

    private final Comparator<ReviewCommentEntity> likesAndIdDescending = Comparator.comparing(
                    ReviewCommentEntity::getLike).reversed()        // 좋아요 많은 순
            .thenComparing(                                         // id 내림차순
                    Comparator.comparing(ReviewCommentEntity::getReviewCommentId).reversed());

    @Test
    @DisplayName("어느 영화 리뷰의 모든 부모 댓글을 최신순으로 검색")
    void findParentCommentByReviewIdOnDateDescend() {
        log.info("<- findParentCommentByReviewIdOnDateDescend");

        // 날짜 최신순, id 내림차순
        Comparator<ReviewCommentEntity> requiredOrder = latestAndIdDescending;

        // 확인 용 데이터 insert
        log.info("Saving new parent review comments");

        List<ReviewCommentEntity> testEntities = insertTestEntities(requiredOrder);

        // 검색 결과 받음
        List<ReviewCommentEntity> searchResult = reviewCommentRepo.findParentCommentByReviewIdOnDateDescend(
                reviewId);

        // 모든 검색 결과 not null 확인
        searchResult.forEach(this::checkNonNullValidation);

        // 모두 부모 댓글인지 확인
        searchResult.forEach(r -> assertThat(r.getGroupId()).isNull());

        // 검색 결과가 내림차순인지 확인
        assertThat(searchResult).isSortedAccordingTo(requiredOrder);

        // 검색 결과에 넣었던 데이터 모두 존재하는지 확인
        assertThat(searchResult).containsAll(testEntities);

        log.info(
                "-> findParentCommentByReviewIdOnDateDescend test passed");
    }

    @Test
    @DisplayName("어느 영화 리뷰의 모든 부모 댓글을 좋아요 순으로 검색")
    void findParentCommentByReviewIdOnLikeDescend() {
        log.info(
                "<- findParentCommentByReviewIdOnLikeDescend");

        // 좋아요 많은 순, id 내림차순
        Comparator<ReviewCommentEntity> requiredOrder = likesAndIdDescending;

        // 확인 용 데이터 insert
        log.info("Saving new parent review comments");

        List<ReviewCommentEntity> testEntities = insertTestEntities(requiredOrder);

        // 검색 결과 받음
        List<ReviewCommentEntity> searchResult = reviewCommentRepo.findParentCommentByReviewIdOnLikeDescend(
                reviewId);

        // 모든 검색 결과 not null 확인
        searchResult.forEach(this::checkNonNullValidation);

        // 모두 부모 댓글인지 확인
        searchResult.forEach(r -> assertThat(r.getGroupId()).isNull());

        // 검색 결과가 내림차순인지 확인
        assertThat(searchResult).isSortedAccordingTo(requiredOrder);

        // 검색 결과에 넣었던 데이터 모두 존재하는지 확인
        assertThat(searchResult).containsAll(testEntities);

        log.info(
                "-> findParentCommentByReviewIdOnLikeDescend test passed");
    }

    @Test
    @DisplayName("특정 부모 댓글의 모든 자식 댓글을 검색 (최신순 정렬 default)")
    void findChildCommentsByGroupId() {
        log.info(
                "<- findChildCommentsByGroupId");

        Comparator<ReviewCommentEntity> requiredOrder = latestAndIdDescending;

        // 확인 용 데이터 insert
        log.info("Saving new child review comments");

        List<ReviewCommentEntity> testEntities = insertTestEntities(testGroupId, null,
                requiredOrder);

        // 검색 결과 받음
        List<ReviewCommentEntity> searchResult = reviewCommentRepo.findChildCommentsByGroupId(
                testGroupId);

        // 모든 검색 결과 not null 확인
        searchResult.forEach(this::checkNonNullValidation);

        // 모두 자식 댓글이고 부모 ID 잘 들어갔는지 확인
        searchResult.forEach(r -> assertThat(r.getGroupId()).isNotNull().isEqualTo(testGroupId));

        // 검색 결과가 내림차순인지 확인
        assertThat(searchResult).isSortedAccordingTo(requiredOrder);

        // 검색 결과에 넣었던 데이터 모두 존재하는지 확인
        assertThat(searchResult).containsAll(testEntities);

        log.info(
                "-> findChildCommentsByGroupId test passed");
    }

    @Test
    @DisplayName("DB 의 모든 부모 댓글을 검색")
    void selectAllParentComments() {
        log.info("<- selectAllParentComments");

        Comparator<ReviewCommentEntity> idAcs = Comparator.comparing(
                ReviewCommentEntity::getReviewCommentId);

        // 확인 용 데이터 insert
        log.info("Saving dummy review comments");

        List<ReviewCommentEntity> parentTestEntities = insertTestEntities(idAcs);
        List<ReviewCommentEntity> childTestEntities = insertTestEntities(testGroupId, null, idAcs);

        // 검색 결과 받기
        List<ReviewCommentEntity> searchResult = reviewCommentRepo.selectAllParentComments()
                .stream()
                .sorted(idAcs)
                .toList();

        // 모든 검색 결과 not null 확인
        searchResult.forEach(this::checkNonNullValidation);

        // 모두 부모 댓글인지 확인
        searchResult.forEach(r -> assertThat(r.getGroupId()).isNull());

        // 입력한 모든 부모 댓글이 있는지 확인
        assertThat(searchResult).containsAll(parentTestEntities);

        // 자식 댓글이 없는지 확인
        assertThat(searchResult).doesNotContainAnyElementsOf(childTestEntities);

        log.info(
                "-> selectAllParentComments test passed");
    }

    @Test
    @DisplayName("DB 의 모든 댓글을 검색")
    void selectAll() {
        log.info("<- selectAll");

        Comparator<ReviewCommentEntity> idAcs = Comparator.comparing(
                ReviewCommentEntity::getReviewCommentId);

        // 확인 용 데이터 insert
        log.info("Saving dummy review comments");

        List<ReviewCommentEntity> testEntities = new ArrayList<>(insertTestEntities(idAcs));
        testEntities.addAll(insertTestEntities(testGroupId, null, idAcs));

        // 검색 결과 받기
        List<ReviewCommentEntity> searchResult = reviewCommentRepo.selectAll()
                .stream()
                .sorted(idAcs)
                .toList();

        // 모든 검색 결과 not null 확인
        searchResult.forEach(this::checkNonNullValidation);

        // 입력한 모든 댓글이 있는지 확인
        assertThat(searchResult).containsAll(testEntities);

        log.info(
                "-> selectAll test passed");
    }

    private static final UUID userId2 = UUID.fromString("cd1fcf52-7e5e-11ef-acea-00d861a152a7");
    private static final Long reviewId2 = 3L;

    @Test
    @DisplayName("특정 댓글의 정보를 변경")
    void editReviewCommentInfo() {
        log.info("<- editReviewCommentInfo");

        ReviewCommentEntity testEntity = reviewCommentRepo.saveNewReviewComment(genTestEntity());

        String replacedContent = "Replaced content!!1243512i3458uy2394";
        Long replacedCommentRef = 772342L;

        ReviewCommentEntity replacement = ReviewCommentEntity.builder().
                content(replacedContent)
                .commentRef(replacedCommentRef)
                .build();

        ReviewCommentEntity editResult = reviewCommentRepo.editReviewCommentInfo(
                testEntity.getReviewCommentId(), replacement);

        // not null 확인
        checkNonNullValidation(editResult);

        // 기존 정보 훼손 안됐는지 확인
        assertThat(editResult).satisfies(
                r -> assertThat(r.getReviewCommentId()).isEqualTo(testEntity.getReviewCommentId()),
                r -> assertThat(r.getReviewId()).isEqualTo(testEntity.getReviewId()),
                r -> assertThat(r.getUserId()).isEqualTo(testEntity.getUserId()),
                r -> assertThat(r.getGroupId()).isEqualTo(testEntity.getGroupId()),
                r -> assertThat(r.getLike()).isEqualTo(testEntity.getLike()),
                r -> assertThat(r.getCreatedAt()).isEqualTo(testEntity.getCreatedAt())
        );

        // 다른 정보 수정 되었는지 확인
        assertThat(editResult).satisfies(
                r -> assertThat(r.getContent()).isEqualTo(replacedContent),
                r -> assertThat(r.getCommentRef()).isEqualTo(replacedCommentRef),
                r -> assertThat(r.isUpdated()).isEqualTo(true)
        );

        log.info(
                "-> editReviewCommentInfo test passed");
    }
}