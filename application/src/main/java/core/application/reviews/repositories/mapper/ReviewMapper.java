package core.application.reviews.repositories.mapper;

import core.application.reviews.models.entities.ReviewEntity;
import core.application.reviews.repositories.ReviewRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper extends ReviewRepository {

    ReviewEntity saveNewReview(@Param("movieId") String movieId,
                               @Param("userId") UUID userId,
                               @Param("review") ReviewEntity review);

    Optional<ReviewEntity> findByReviewId(Long reviewId);

    List<ReviewEntity> findByMovieId(String movieId);

    List<ReviewEntity> findByMovieIdOnDateDescend(String movieId);

    List<ReviewEntity> findByMovieIdOnLikeDescend(String movieId);

    List<ReviewEntity> findByMovieIdWithoutContent(String movieId);

    List<ReviewEntity> findByMovieIdWithoutContentOnDateDescend(String movieId);

    List<ReviewEntity> findByMovieIdWithoutContentOnLikeDescend(String movieId);

    List<ReviewEntity> findByUserId(UUID userId);

    List<ReviewEntity> selectAll();

    ReviewEntity editReviewInfo(Long reviewId, ReviewEntity reviewEntity);

    void deleteReview(Long reviewId);
}
