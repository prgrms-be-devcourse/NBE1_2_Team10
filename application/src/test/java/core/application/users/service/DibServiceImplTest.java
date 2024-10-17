package core.application.users.service;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.movie.CachedMovieRepository;
import core.application.users.models.dto.DibRespDTO;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class DibServiceImplTest {

    @Autowired
    private DibService dibService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CachedMovieRepository movieRepo;

    private UserEntity user;
    private CachedMovieEntity movie;

    @BeforeEach
    public void setup() {
        user = UserEntity.builder()
                .userEmail("testEmail")
                .userPw("test")
                .role(UserRole.USER)
                .alias("nickname")
                .phoneNum("phone")
                .userName("test")
                .build();
        userRepo.saveNewUser(user);

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
    @DisplayName("찜 생성하기")
    @Transactional
    void dibCreate() {
        // Given
        CachedMovieEntity find = movieRepo.findByMovieId(movie.getMovieId()).orElseThrow();

        // When
        DibRespDTO dibRespDTO = dibService.dibProcess(user.getUserId(), movie.getMovieId());

        // Then
//        System.out.println(dibRespDTO.getMessage());
//        System.out.println(dibRespDTO.getUserId());
//        System.out.println(dibRespDTO.getMovieId());
//        System.out.println(movie.getDibCount());
        CachedMovieEntity dibMovie = movieRepo.findByMovieId(movie.getMovieId()).orElseThrow();
        assertThat(dibRespDTO.getMessage().equals("찜 완료되었습니다."));
        assertThat(dibRespDTO.getUserId().equals(user.getUserId()));
        assertThat(dibRespDTO.getMovieId().equals(movie.getMovieId()));
        assertThat(dibMovie.getDibCount() == movie.getDibCount() + 1);
    }

    @Test
    @DisplayName("찜 취소하기")
    @Transactional
    void dibCancel() {
        // Given
        DibRespDTO dib = dibService.dibProcess(user.getUserId(), movie.getMovieId()); // 이미 찜 생성된 상태
        Long beforeDibCount = movieRepo.findByMovieId(movie.getMovieId()).orElseThrow().getDibCount();

        // When
        DibRespDTO undib = dibService.dibProcess(user.getUserId(), movie.getMovieId());// 다시 찜 눌렀을 때
        Long afterDibCount = movieRepo.findByMovieId(movie.getMovieId()).orElseThrow().getDibCount();

        // Then

        assertThat(undib.getMessage().equals("찜 취소 완료되습니다."));
        assertThat(undib.getUserId().equals(dib.getUserId()));
        assertThat(undib.getMovieId().equals(dib.getMovieId()));
        assertThat(beforeDibCount - afterDibCount).isEqualTo(1);
    }
}
