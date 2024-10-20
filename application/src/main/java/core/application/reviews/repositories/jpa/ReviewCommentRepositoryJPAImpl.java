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
public class ReviewCommentRepositoryJPAImpl implements ReviewCommentRepository {

    private final JpaReviewCommentRepository jpaRepo;

    @Override
    public ReviewCommentEntity saveNewReviewComment(ReviewCommentEntity reviewComment) {
        return null;
    }

    @Override
    public ReviewCommentEntity saveNewParentReviewComment(Long reviewId, UUID userId,
            ReviewCommentEntity reviewComment) {
        return null;
    }

    @Override
    public ReviewCommentEntity saveNewChildReviewComment(Long groupId, UUID userId,
            ReviewCommentEntity reviewComment) {
        return null;
    }

    @Override
    public Optional<ReviewCommentEntity> findByReviewCommentId(Long reviewCommentId) {
        return Optional.empty();
    }

    @Override
    public List<ReviewCommentEntity> findParentCommentByReviewId(Long reviewId, int offset,
            int num) {
        return List.of();
    }

    @Override
    public List<ReviewCommentEntity> findParentCommentByReviewIdOnDateDescend(Long reviewId,
            int offset, int num) {
        return List.of();
    }

    @Override
    public List<ReviewCommentEntity> findParentCommentByReviewIdOnLikeDescend(Long reviewId,
            int offset, int num) {
        return List.of();
    }

    @Override
    public long countParentCommentByReviewId(Long reviewId) {
        return 0;
    }

    @Override
    public List<ReviewCommentEntity> findChildCommentsByGroupId(Long groupId, int offset, int num) {
        return List.of();
    }

    @Override
    public long countChildCommentByGroupId(Long groupId) {
        return 0;
    }

    @Override
    public List<ReviewCommentEntity> selectAllParentComments() {
        return List.of();
    }

    @Override
    public List<ReviewCommentEntity> selectAll() {
        return List.of();
    }

    @Override
    public ReviewCommentEntity editReviewCommentInfo(Long reviewCommentId,
            ReviewCommentEntity replacement) {
        return null;
    }

    @Override
    public ReviewCommentEntity editReviewCommentInfo(Long reviewCommentId,
            ReviewCommentEntity replacement, boolean update) {
        return null;
    }

    @Override
    public ReviewCommentEntity updateReviewCommentLikes(Long reviewCommentId, int likes) {
        return null;
    }
}
