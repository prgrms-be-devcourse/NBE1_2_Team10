package core.application.reviews.services;

import core.application.reviews.exceptions.NoReviewCommentFoundException;
import core.application.reviews.exceptions.NoReviewFoundException;
import core.application.reviews.models.entities.ReviewCommentEntity;
import core.application.reviews.repositories.ReviewCommentRepository;
import core.application.reviews.repositories.ReviewRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewCommentServiceImpl implements
        ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepo;
    private final ReviewRepository reviewRepo;      // 아직 bean 으로 등록 안되어 있어서 build 에러 날 수 있음.

    /**
     * 주어진 {@code id} 로 {@code function} 호출했을 때 값이 존재하는지 아닌지 확인하는 메서드
     *
     * @param id        검사할 ID
     * @param function  호출할 함수 {@code (Long -> Optional<?>}
     * @param exception 부재시 {@code throw} 할 exception {@code (() -> RuntimeException)}
     */
    private static <R> R doesExist(Long id,
            Function<Long, Optional<R>> function,
            Supplier<? extends RuntimeException> exception) {
        return function.apply(id).orElseThrow(exception);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReviewCommentEntity> getParentReviewComments(Long reviewId,
            ReviewCommentSortOrder order) throws NoReviewFoundException {

        // reviewId 에 해당하는 포스팅 없으면 throw
        doesExist(reviewId, reviewRepo::findByReviewId, () -> new NoReviewFoundException(reviewId));

        return switch (order) {
            // 최신순
            case LATEST -> reviewCommentRepo.findParentCommentByReviewIdOnDateDescend(reviewId);
            // 좋아요 순
            case LIKE -> reviewCommentRepo.findParentCommentByReviewIdOnLikeDescend(reviewId);
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReviewCommentEntity> getChildReviewCommentsOnParent(Long groupId) {
        return reviewCommentRepo.findChildCommentsByGroupId(groupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewCommentEntity addNewParentReviewComment(Long reviewId, UUID userId,
            ReviewCommentEntity parentReviewComment) throws NoReviewFoundException {

        // 포스팅 없으면 throw
        doesExist(reviewId, reviewRepo::findByReviewId, () -> new NoReviewFoundException(reviewId));

        return reviewCommentRepo.saveNewParentReviewComment(reviewId, userId, parentReviewComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewCommentEntity addNewChildReviewComment(Long reviewId, Long groupId, UUID userId,
            ReviewCommentEntity childReviewComment)
            throws NoReviewFoundException, NoReviewCommentFoundException {

        // 포스팅, 부모 댓글 없으면 throw
        doesExist(reviewId, reviewRepo::findByReviewId, () -> new NoReviewFoundException(reviewId));
        doesExist(groupId, reviewCommentRepo::findByReviewCommentId,
                () -> new NoReviewCommentFoundException(groupId));

        ReviewCommentEntity validData = ReviewCommentEntity.builder()
                .reviewId(reviewId)
                .userId(userId)
                .content(childReviewComment.getContent())
                .groupId(groupId)
                .commentRef(childReviewComment.getCommentRef())
                .build();

        return reviewCommentRepo.saveNewChildReviewComment(groupId, userId, validData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewCommentEntity addReviewCommentMention(ReviewCommentEntity origin,
            ReviewCommentEntity target) {
        return origin.mentionReviewComment(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewCommentEntity editReviewComment(Long reviewCommentId, Long commentRef,
            String contentReplacement)
            throws NoReviewCommentFoundException {

        ReviewCommentEntity origin = doesExist(reviewCommentId,
                reviewCommentRepo::findByReviewCommentId,
                () -> new NoReviewCommentFoundException(reviewCommentId));

        if (commentRef != null) {
            origin.mentionReviewComment(
                    doesExist(commentRef, reviewCommentRepo::findByReviewCommentId,
                            () -> new NoReviewCommentFoundException(reviewCommentId))
            );
        }

        ReviewCommentEntity replacement = ReviewCommentEntity.builder()
                .content(contentReplacement)
                .commentRef(origin.getCommentRef())
                .build();

        return reviewCommentRepo.editReviewCommentInfo(reviewCommentId, replacement, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewCommentEntity deleteReviewComment(Long reviewCommentId)
            throws NoReviewCommentFoundException {

        doesExist(reviewCommentId, reviewCommentRepo::findByReviewCommentId,
                () -> new NoReviewCommentFoundException(reviewCommentId));

        ReviewCommentEntity validData = ReviewCommentEntity.builder()
                .content("해당 댓글은 삭제되었습니다.")
                .commentRef(null)
                .build();

        return reviewCommentRepo.editReviewCommentInfo(reviewCommentId, validData, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewCommentEntity increaseCommentLike(Long reviewCommentId)
            throws NoReviewCommentFoundException {

        int likeOrigin = doesExist(reviewCommentId, reviewCommentRepo::findByReviewCommentId,
                () -> new NoReviewCommentFoundException(reviewCommentId)).getLike();

        return reviewCommentRepo.updateReviewCommentLikes(reviewCommentId, ++likeOrigin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReviewCommentEntity decreaseCommentLike(Long reviewCommentId)
            throws NoReviewCommentFoundException {

        int likeOrigin = doesExist(reviewCommentId, reviewCommentRepo::findByReviewCommentId,
                () -> new NoReviewCommentFoundException(reviewCommentId)).getLike();

        return reviewCommentRepo.updateReviewCommentLikes(reviewCommentId,
                likeOrigin <= 0 ? 0 : --likeOrigin);
    }
}
