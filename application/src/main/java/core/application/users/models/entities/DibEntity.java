package core.application.users.models.entities;

import core.application.movies.models.entities.CachedMovieEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "dib_table")
public class DibEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   dibId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private CachedMovieEntity movie;

    public static DibEntity of(UserEntity user, CachedMovieEntity movie) {
        return DibEntity.builder()
                .user(user)
                .movie(movie)
                .build();
    }
}
