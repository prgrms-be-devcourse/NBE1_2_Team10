package core.application.movies.repositories.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import core.application.movies.models.entities.CachedMovieEntity;

@Mapper
public interface CachedMovieMapper {

	CachedMovieEntity save(CachedMovieEntity movie);

	Optional<CachedMovieEntity> findByMovieId(String movieId);

	List<CachedMovieEntity> selectOnDibOrderDescend();

	List<CachedMovieEntity> selectOnDibOrderDescendLimit(int num);

	List<CachedMovieEntity> selectOnAVGRatingDescend();

	List<CachedMovieEntity> selectOnAVGRatingDescendLimit(int num);

	CachedMovieEntity update(@Param("movieId") String movieId, @Param("replacement") CachedMovieEntity replacement);

	void delete(String movieId);
}
