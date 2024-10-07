package core.application.movies.models.dto;

import org.json.JSONObject;

import core.application.movies.models.entities.CachedMovieEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "영화 검색 응답")
public class MovieSearchRespDTO {

	@Schema(description = "영화 ID", example = "A-12345")
	private String movieId;

	@Schema(description = "영화 제목", example = "범죄도시")
	private String title;

	@Schema(description = "포스터 URL", example = "포스터 URL")
	private String posterUrl;

	@Schema(description = "제작년도", example = "2015")
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
