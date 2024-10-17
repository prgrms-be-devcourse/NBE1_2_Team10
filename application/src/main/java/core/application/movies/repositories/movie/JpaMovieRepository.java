package core.application.movies.repositories.movie;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import core.application.movies.models.entities.CachedMovieEntity;

public interface JpaMovieRepository extends JpaRepository<CachedMovieEntity, String> {
	@Query("select m from CachedMovieEntity m order by m.dibCount desc limit :num")
	List<CachedMovieEntity> findTopXXOrderByDibCount(int num);

	@Query("select m from CachedMovieEntity m order by m.sumOfRating / m.commentCount desc limit :num")
	List<CachedMovieEntity> findTopXXOrderByAvgRating(int num);

	@Query("select m from CachedMovieEntity m order by m.sumOfRating / m.commentCount")
	List<CachedMovieEntity> findAllOrderByAvgRating();

	@Query("select m from CachedMovieEntity m order by m.reviewCount desc limit :num")
	List<CachedMovieEntity> findTopXXOrderByReviewCount(int num);


	@Query("select m from CachedMovieEntity m where m.genre like %:genre% order by m.sumOfRating / m.commentCount")
	Page<CachedMovieEntity> findByGenreOrderByAvgRating(String genre, Pageable pageable);

}
