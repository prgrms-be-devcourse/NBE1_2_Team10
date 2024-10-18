package core.application.movies.repositories.movie.jpa;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.movie.CachedMovieRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCachedMovieRepositoryImpl implements CachedMovieRepository {

    private final JpaCachedMovieRepository jpaCachedMovieRepository;

    @Override
    public CachedMovieEntity saveNewMovie(CachedMovieEntity movie) {
        return jpaCachedMovieRepository.save(movie);
    }

    @Override
    public Optional<CachedMovieEntity> findByMovieId(String movieId) {
        return jpaCachedMovieRepository.findById(movieId);
    }

    @Override
    public List<CachedMovieEntity> selectOnDibOrderDescend() {
        return jpaCachedMovieRepository.findAllOrderBy(Sort.by(Sort.Direction.DESC, "dib_count"));
    }

    @Override
    public List<CachedMovieEntity> selectOnDibOrderDescend(int num) {
        return jpaCachedMovieRepository.findTopXOrderBy(PageRequest.of(0, num, Sort.by(Sort.Direction.DESC, "dib_count")));
    }

    @Override
    public List<CachedMovieEntity> selectOnAVGRatingDescend() {
        return jpaCachedMovieRepository.findAllOrderByAvgRating();
    }

    @Override
    public List<CachedMovieEntity> selectOnAVGRatingDescend(int num) {
        return jpaCachedMovieRepository.findTopXOrderByAvgRating(PageRequest.of(0, num));
    }

    @Override
    public List<CachedMovieEntity> selectOnReviewCountDescend(int num) {
        return jpaCachedMovieRepository.findTopXOrderBy(PageRequest.of(0, num, Sort.by(Sort.Direction.DESC, "review_count")));
    }

    @Override
    public List<CachedMovieEntity> findMoviesOnRatingDescendWithGenre(int offset, String genre) {
        return List.of();
    }

    @Override
    public Page<CachedMovieEntity> findMoviesLikeGenreOrderByAvgRating(int page, String genre) {
        return jpaCachedMovieRepository.findByGenreOrderByAvgRating(genre, PageRequest.of(page, 10));
    }

    @Override
    public int countGenreMovie(String genre) {
        return 0;
    }

    @Override
    public CachedMovieEntity editMovie(String movieId, CachedMovieEntity replacement) {
        return jpaCachedMovieRepository.save(replacement);
    }

    @Override
    public void deleteMovie(String movieId) {
        jpaCachedMovieRepository.deleteById(movieId);
    }
}
