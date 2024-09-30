package core.application.movies.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.mapper.CachedMovieMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CachedMovieRepositoryImpl implements CachedMovieRepository {

	private final CachedMovieMapper mapper;

	@Override
	public CachedMovieEntity saveNewMovie(CachedMovieEntity movie) {
		mapper.save(movie);
		return movie;
	}

	@Override
	public Optional<CachedMovieEntity> findByMovieId(String movieId) {
		return mapper.findByMovieId(movieId);
	}

	@Override
	public List<CachedMovieEntity> selectOnDibOrderDescend() {
		return mapper.selectOnDibOrderDescend();
	}

	@Override
	public List<CachedMovieEntity> selectOnDibOrderDescend(int num) {
		return mapper.selectOnDibOrderDescendLimit(num);
	}

	@Override
	public List<CachedMovieEntity> selectOnAVGRatingDescend() {
		return mapper.selectOnAVGRatingDescend();
	}

	@Override
	public List<CachedMovieEntity> selectOnAVGRatingDescend(int num) {
		return mapper.selectOnAVGRatingDescendLimit(num);
	}

	@Override
	public List<CachedMovieEntity> selectOnReviewCountDescend(int num) {
		return mapper.selectOnReviewCountDescend(num);
	}

	@Override
	public CachedMovieEntity editMovie(String movieId, CachedMovieEntity replacement) {
		mapper.update(movieId, replacement);
		return replacement;
	}

	@Override
	public void deleteMovie(String movieId) {
		mapper.delete(movieId);
	}
}
