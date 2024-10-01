package core.application.movies.models.dto;

import core.application.movies.models.entities.CachedMovieEntity;
import lombok.Data;

@Data
public class MovieDetailRespDTO {
	private String movieId;
	private String title;
	private String posterUrl;
	private String genre;
	private String releaseDate;
	private String plot;
	private String runningTime;
	private String actors;
	private String director;
	private Long   dibCount;
	private Long   reviewCount;
	private Long   commentCount;
	private Long   sumOfRating;

	public MovieDetailRespDTO toDTO(CachedMovieEntity cachedMovieEntity){
		this.movieId = cachedMovieEntity.getMovieId();
		this.title = cachedMovieEntity.getTitle();
		this.posterUrl = cachedMovieEntity.getPosterUrl();
		this.genre = cachedMovieEntity.getGenre();
		this.releaseDate = cachedMovieEntity.getReleaseDate();
		this.plot = cachedMovieEntity.getPlot();
		this.runningTime = cachedMovieEntity.getRunningTime();
		this.actors = cachedMovieEntity.getActors();
		this.director = cachedMovieEntity.getDirector();
		this.dibCount = cachedMovieEntity.getDibCount();
		this.reviewCount = cachedMovieEntity.getReviewCount();
		this.commentCount = cachedMovieEntity.getCommentCount();
		this.sumOfRating = cachedMovieEntity.getSumOfRating();

		return this;
	}
}
