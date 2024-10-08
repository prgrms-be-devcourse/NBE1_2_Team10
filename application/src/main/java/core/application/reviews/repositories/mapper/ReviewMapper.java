package core.application.reviews.repositories.mapper;

import core.application.reviews.models.entities.ReviewEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {

    int saveNewReview(
            @Param("movieId") String movieId,
            @Param("userId") UUID userId,
            @Param("review") ReviewEntity review
    );

    Optional<ReviewEntity> findByReviewId(Long reviewId);

    List<ReviewEntity> findByMovieId(
            @Param("movieId") String movieId,
            @Param("offset") int offset,
            @Param("num") int num
    );

    List<ReviewEntity> findByMovieIdOnDateDescend(
            @Param("movieId") String movieId,
            @Param("offset") int offset,
            @Param("num") int num
    );

    List<ReviewEntity> findByMovieIdOnLikeDescend(
            @Param("movieId") String movieId,
            @Param("offset") int offset,
            @Param("num") int num
    );

    List<ReviewEntity> findByMovieIdWithoutContent(
            @Param("movieId") String movieId,
            @Param("offset") int offset,
            @Param("num") int num
    );

    List<ReviewEntity> findByMovieIdWithoutContentOnDateDescend(
            @Param("movieId") String movieId,
            @Param("offset") int offset,
            @Param("num") int num
    );

    List<ReviewEntity> findByMovieIdWithoutContentOnLikeDescend(
            @Param("movieId") String movieId,
            @Param("offset") int offset,
            @Param("num") int num
    );

    List<ReviewEntity> findByUserId(UUID userId);

    List<ReviewEntity> selectAll();

    int editReviewInfo(
            @Param("reviewId") Long reviewId,
            @Param("replacement") ReviewEntity replacement);

    void deleteReview(Long reviewId);
}
