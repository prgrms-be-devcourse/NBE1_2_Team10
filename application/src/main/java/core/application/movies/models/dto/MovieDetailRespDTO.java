package core.application.movies.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDetailRespDTO {
	private String movieId;

	private String title;

	private String posterUrl;

	private String genre;

	private String releaseDate;

	private String overview;

	private String runningTime;

	private List<String> actors;

	private String director;

	private Integer dibCnt;

	private String rating;
}
