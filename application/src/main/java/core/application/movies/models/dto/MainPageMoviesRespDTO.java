package core.application.movies.models.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MainPageMoviesRespDTO {
	private List<MainPageMovieRespDTO> dibMovieList;

	private List<MainPageMovieRespDTO> ratingMovieList;

	private List<MainPageMovieRespDTO> reviewMovieList;

	public static MainPageMoviesRespDTO of(List<MainPageMovieRespDTO> dib,
		List<MainPageMovieRespDTO> rating,
		List<MainPageMovieRespDTO> review) {
		return MainPageMoviesRespDTO.builder()
			.dibMovieList(dib)
			.ratingMovieList(rating)
			.reviewMovieList(review)
			.build();
	}
}
