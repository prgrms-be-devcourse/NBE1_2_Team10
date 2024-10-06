package core.application.movies.models.dto;

import org.json.JSONObject;

import core.application.movies.models.entities.CachedMovieEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieSearchRespDTO {
	private String movieId;

	private String title;

	private String posterUrl;

	private String producedYear;

	public static MovieSearchRespDTO from(JSONObject movie) {
		return MovieSearchRespDTO.builder()
			.movieId(movie.optString("movieId") + "-" + movie.optString("movieSeq"))
			.title(movie.optString("title"))
			.posterUrl(movie.optString("posters").split("\\|")[0])
			.producedYear(movie.optString("prodYear"))
			.build();
	}

	public static MovieSearchRespDTO from(CachedMovieEntity movie) {
		return MovieSearchRespDTO.builder()
			.movieId(movie.getMovieId())
			.title(movie.getTitle())
			.posterUrl(movie.getPosterUrl())
			.producedYear(movie.getReleaseDate())
			.build();
	}
}
