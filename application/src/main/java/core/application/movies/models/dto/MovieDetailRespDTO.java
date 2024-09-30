package core.application.movies.models.dto;

import core.application.movies.constant.Genre;
import core.application.movies.models.entities.CachedMovieEntity;
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

	private String actors;

	private String director;

	private Long dibCnt;

	private Long reviewCount;

	private Long commentCount;

	private Long rating;

	public MovieDetailRespDTO toDTO(CachedMovieEntity cachedMovieEntity){
		this.movieId = cachedMovieEntity.getMovieId();
		this.title = cachedMovieEntity.getTitle();
		this.posterUrl = cachedMovieEntity.getPosterUrl();
		this.genre = cachedMovieEntity.getGenre();
		this.releaseDate = cachedMovieEntity.getReleaseDate();
		this.overview = cachedMovieEntity.getPlot();
		this.runningTime = cachedMovieEntity.getRunningTime();
		this.actors = cachedMovieEntity.getActors();
		this.director = cachedMovieEntity.getDirector();
		this.dibCnt = cachedMovieEntity.getDibCount();
		this.reviewCount = cachedMovieEntity.getReviewCount();
		this.commentCount = cachedMovieEntity.getCommentCount();
		this.rating = cachedMovieEntity.getSumOfRating();

		return this;
	}
}
