package core.application.users.repositories;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.movie.CachedMovieRepository;
import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DibRepositoryImplTest {

    @Autowired
    private DibRepository dibRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CachedMovieRepository movieRepo;

    private CachedMovieEntity movie;
    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    public void setup() {
        user1 = UserEntity.builder()
                .userEmail("testEmail")
                .userPw("test")
                .role(UserRole.USER)
                .alias("nickname")
                .phoneNum("phone")
                .userName("test")
                .build();
        userRepo.saveNewUser(user1);

        user2 = UserEntity.builder()
                .userEmail("test2")
                .userPw("test")
                .role(UserRole.USER)
                .alias("nickname")
                .phoneNum("phone")
                .userName("test")
                .build();
        userRepo.saveNewUser(user2);

        movie = new CachedMovieEntity(
                "test",
                "testTitle",
                "posterUrl",
                "액션",
                "2024-09-30",
                "줄거리",
                "122",
                "마동석, 김무열",
                "봉준호",
                1L, 1L, 10L, 10L
        );
        movieRepo.saveNewMovie(movie);
    }

    @Test
    @DisplayName("찜하기")
    @Transactional
    void saveNewDib() {
        dibRepo.saveNewDib(user1.getUserId(), movie.getMovieId());

        // When
        DibEntity find = dibRepo.findByUserIdAndMovieId(user1.getUserId(), movie.getMovieId()).orElseThrow();
        System.out.println(find);

        // Then
        assertThat(find.getUser().getUserId() == UUID.fromString("b7172110-7e18-11ef-8da5-467268b55380"));
        assertThat(find.getMovie().getMovieId().equals(movie.getMovieId()));
    }

    @Test
    @DisplayName("찜 아이디로 찜 찾기")
    @Transactional
    void findByDibId() {
        // Given

        dibRepo.saveNewDib(user1.getUserId(), movie.getMovieId());
        Long dibId = dibRepo.findByUserIdAndMovieId(user1.getUserId(), movie.getMovieId()).get().getDibId();

        // When
        DibEntity find = dibRepo.findByDibId(dibId).orElseThrow();

        // Then
        assertThat(find.getDibId()).isEqualTo(dibId);
        assertThat(find.getUser().getUserId()).isEqualTo(user1.getUserId());
        assertThat(find.getMovie().getMovieId()).isEqualTo(movie.getMovieId());
    }

    @Test
    @DisplayName("특정 영화의 찜 개수 세기")
    @Transactional
    void countMovie() {
        // Given
        dibRepo.saveNewDib(user1.getUserId(), movie.getMovieId());
        dibRepo.saveNewDib(user2.getUserId(), movie.getMovieId());

        // When
        Long num = dibRepo.countMovie(movie.getMovieId());

        // Then
        assertThat(num).isEqualTo(2);
    }

    @Test
    @DisplayName("찜 객체 리스트 전체 조회")
    @Transactional
    void selectAll() {
        // Given
        dibRepo.saveNewDib(user1.getUserId(), movie.getMovieId());
        dibRepo.saveNewDib(user2.getUserId(), movie.getMovieId());

        Long dibId1 = dibRepo.findByUserIdAndMovieId(user1.getUserId(), movie.getMovieId()).get().getDibId();
        Long dibId2 = dibRepo.findByUserIdAndMovieId(user2.getUserId(), movie.getMovieId()).get().getDibId();

        // When
        List<DibEntity> dibs = dibRepo.selectAll();

        // Then
        assertThat(dibs.get(0).getDibId().equals(dibId1));
        assertThat(dibs.get(0).getUser().getUserId() == user1.getUserId());
        assertThat(dibs.get(0).getMovie().getMovieId().equals(movie.getMovieId()));

        assertThat(dibs.get(1).getDibId().equals(dibId2));
        assertThat(dibs.get(1).getUser().getUserId() == user2.getUserId());
        assertThat(dibs.get(1).getMovie().getMovieId().equals(movie.getMovieId()));
    }

    @Test
    @DisplayName("찜 아이디로 찜 삭제하기")
    @Transactional
    void deleteDibByDibId() {
        // Given
        dibRepo.saveNewDib(user1.getUserId(), movie.getMovieId());

        // When
        Long dibId = dibRepo.findByUserIdAndMovieId(user1.getUserId(), movie.getMovieId()).get().getDibId();
        dibRepo.deleteDib(dibId);

        // Then
        assertThat(!dibRepo.findByDibId(dibId).isPresent());
    }

    @Test
    @DisplayName("유저 아이디로 찜 삭제하기")
    @Transactional
    void DeleteDibByUserId() {
        // Given
        dibRepo.saveNewDib(user1.getUserId(), movie.getMovieId());

        // When
        dibRepo.deleteDib(user1.getUserId());

        // Then
        assertThat(!dibRepo.findByUserIdAndMovieId(user1.getUserId(), movie.getMovieId()).isPresent());
    }

    @Test
    @DisplayName("유저 아이디랑 영화 아이디로 찜 삭제하기")
    @Transactional
    void DeleteDibByUserIdAndMovieId() {
        // Given
        dibRepo.saveNewDib(user1.getUserId(), movie.getMovieId());

        // When
        dibRepo.deleteDib(user1.getUserId(), movie.getMovieId());

        // Then
        assertThat(!dibRepo.findByUserIdAndMovieId(user1.getUserId(), movie.getMovieId()).isPresent());
    }
}
