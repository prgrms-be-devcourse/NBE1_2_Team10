package core.application.movies.models.dto;

import org.json.JSONObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieSearchRespDTO {
	private String movieId;

	private String title;

	private String posterUrl;

	private String releaseDate;

	public static MovieSearchRespDTO from(JSONObject movie) {
		return MovieSearchRespDTO.builder()
			.movieId(movie.optString("movieId") + "-" + movie.optString("movieSeq"))
			.title(movie.optString("title"))
			.posterUrl(movie.optString("posters").split("\\|")[0])
			.releaseDate(movie.optString("repRlsDate"))
			.build();
	}
}
