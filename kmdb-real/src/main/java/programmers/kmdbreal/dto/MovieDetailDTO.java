package programmers.kmdbreal.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MovieDetailDTO {
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
