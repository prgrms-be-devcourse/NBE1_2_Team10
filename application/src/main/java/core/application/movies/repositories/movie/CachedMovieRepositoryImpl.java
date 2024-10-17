package core.application.movies.repositories.movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.mapper.CachedMovieMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CachedMovieRepositoryImpl implements CachedMovieRepository {

	private final CachedMovieMapper mapper;
	private final JpaMovieRepository jpaRepository;

	@Override
	public CachedMovieEntity saveNewMovie(CachedMovieEntity movie) {
		return jpaRepository.save(movie);
	}

	@Override
	public Optional<CachedMovieEntity> findByMovieId(String movieId) {
		return jpaRepository.findById(movieId);
	}

	@Override
	public List<CachedMovieEntity> selectOnDibOrderDescend(int num) {
		return jpaRepository.findTopXXOrderByDibCount(num);
	}

	@Override
	public List<CachedMovieEntity> selectOnAVGRatingDescend(int num) {
		return jpaRepository.findTopXXOrderByAvgRating(num);
	}

	@Override
	public List<CachedMovieEntity> selectOnReviewCountDescend(int num) {
		return jpaRepository.findTopXXOrderByReviewCount(num);
	}

	@Override
	public Page<CachedMovieEntity> findMoviesOnRatingDescendWithGenre(int page, String genre) {
		return jpaRepository.findByGenreOrderByAvgRating(genre, PageRequest.of(page, 10));
	}

	@Override
	public CachedMovieEntity editMovie(String movieId, CachedMovieEntity replacement) {
		return null;
	}

	@Override
	public void deleteMovie(String movieId) {
		jpaRepository.deleteById(movieId);
	}
}
