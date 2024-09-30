package core.application.movies.models.entities;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class CachedMovieEntity {
    /**
     * {@code 알파벳}-{@code 숫자} 형태 {@code (KMDB 영화 ID 형태)}
     */
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

    public void incrementDibCount() {
        this.dibCount++;
    }

    public void decrementDibCount() {
        this.dibCount--;
    }

    public void incrementReviewCount() {
        this.reviewCount++;
    }

    public void decrementReviewCount() {
        this.reviewCount--;
    }

    public void isCommentedWithRating(int rating) {
        this.commentCount++;
        this.sumOfRating += rating;
    }

    public void deleteComment(int rating) {
        this.commentCount--;
        this.sumOfRating -= rating;
    }

//    public void saveAPIMovieId(String movieId){
//        this.movieId = movieId;
//    }
//
//    public void saveAPITitle(String movieId){
//        this.title = movieId;
//    }
//
//    public void saveAPIPosterUrl(String posterUrl){
//        this.posterUrl = posterUrl;
//    }
//
//    public void saveAPIGenre(String genre){
//        this.genre = genre;
//    }
//
//    public void saveAPIReleaseDate(String releaseDate){
//        this.releaseDate = releaseDate;
//    }
//
//    public void saveAPIPlot(String plot){
//        this.plot = plot;
//    }
//
//    public void saveAPIRunningTime(String runningTime){
//        this.runningTime = runningTime;
//    }
//
//    public void saveAPIActors(String actors){
//        this.actors = actors;
//    }
//
//    public void saveAPIDirector(String director){
//        this.director = director;
//    }
}
