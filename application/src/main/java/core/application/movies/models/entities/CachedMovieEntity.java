package core.application.movies.models.entities;

import java.util.List;

import core.application.movies.constant.Genre;
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
    private Genre genre;
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
}
