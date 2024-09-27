package core.application.reviews.services;

import core.application.reviews.exceptions.NoReviewFoundException;
import core.application.reviews.models.entities.ReviewEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * 영화 후기 포스팅과 관련된 서비스 인터페이스
 */
public interface ReviewService {

    /**
     * 리뷰글 보기
     *
     * @param reviewId    리뷰 포스팅 id
     * @param order       리뷰 포스팅 정렬 순서 {@code (최신순, 좋아요순)}
     * @param withContent 본문을 포함해서 불러올지 {@code Y/N}
     * @return {@link Optional}{@code <}{@link ReviewEntity}{@code >}
     * @throws NoReviewFoundException {@code reviewId} 에 해당하는 리뷰 포스팅을 찾지 못했을 시
     * @author semin9809
     * @see ReviewSortOrder
     */
    Optional<ReviewEntity> loadReview(Long reviewId, ReviewSortOrder order, boolean withContent)
            throws NoReviewFoundException;


    /**
     * 특정 리뷰 포스팅을 수정하는 서비스
     *
     * @param reviewId     리뷰 포스팅 ID
     * @param updateReview 수정된 리뷰
     * @return {@link ReviewEntity}
     * @throws NoReviewFoundException {@code reviewId} 에 해당하는 리뷰 포스팅을 찾지 못했을 시
     * @author semin9809
     */
    ReviewEntity updateReview(Long reviewId, ReviewEntity updateReview) throws NoReviewFoundException;


    /**
     * 리뷰 삭제하는 서비스
     *
     * @param reviewId 리뷰 포스팅 ID
     * @return {@link ReviewEntity} 삭제된 리뷰 정보
     * @throws NoReviewFoundException {@code reviewId} 에 해당하는 리뷰 포스팅을 찾지 못했을 시
     * @author semin9809
     */
    ReviewEntity deleteReview(Long reviewId) throws NoReviewFoundException;


    /**
     * 리뷰에 좋아요 누르기
     * <p>
     * userId로 확인하여 중복 좋아요 방지
     *
     * @param reviewId 리뷰 포스팅 ID
     * @param userId   사용자 ID
     * @return {@link ReviewEntity} 좋아요가 1 증가된 리뷰 정보
     * @throws NoReviewFoundException {@code reviewId} 에 해당하는 리뷰 포스팅을 찾지 못했을 시
     * @author semin9809
     */
    ReviewEntity likeCount(Long reviewId, UUID userId) throws NoReviewFoundException;

    /**
     * 리뷰에 누른 좋아요 취소하기
     * <p>
     * userId로 확인하여 중복 처리 못하게 하기
     *
     * @param reviewId 리뷰 포스팅 ID
     * @param userId   사용자 ID
     * @return {@link ReviewEntity} 좋아요가 1 감소된 리뷰 정보
     * @throws NoReviewFoundException {@code reviewId} 에 해당하는 리뷰 포스팅을 찾지 못했을 시
     * @author semin9809
     */
    ReviewEntity likeCancel(Long reviewId, UUID userId) throws NoReviewFoundException;

}
