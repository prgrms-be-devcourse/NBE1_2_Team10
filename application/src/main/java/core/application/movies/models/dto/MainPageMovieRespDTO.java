package core.application.movies.models.dto;

import core.application.movies.models.entities.CachedMovieEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MainPageMovieRespDTO {

	private String movieId;

	private String title;

	private String posterUrl;

	private String releaseDate;

	public static MainPageMovieRespDTO from(CachedMovieEntity movie) {
		return MainPageMovieRespDTO.builder()
			.movieId(movie.getMovieId())
			.title(movie.getTitle())
			.posterUrl(movie.getPosterUrl())
			.releaseDate(movie.getReleaseDate())
			.build();
	}
}
