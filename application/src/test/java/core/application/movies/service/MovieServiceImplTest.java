package core.application.movies.service;

import core.application.movies.models.dto.MovieDetailRespDTO;
import core.application.movies.repositories.CachedMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieServiceImplTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private CachedMovieRepository movieRepository;

    @BeforeEach
    public void initTestData() {
    }

    @Test
    @DisplayName("메인 페이지의 영화 목록을 보여준다.")
    public void getMainMovies() {}

    @Test
    @DisplayName("영화 상세 정보 가져오기 서비스 테스트")
    void testGetMovieDetailInfo_MovieExistsInCache() {
        // GIVEN
        String movieId = "K36062";

        // Act
        MovieDetailRespDTO result = movieService.getMovieDetailInfo(movieId);

        // Assert
        assertNotNull(result);
        assertEquals("K36062", result.getMovieId());
        assertEquals("댓글부대", result.getTitle());
        assertEquals("드라마,범죄", result.getGenre());
        assertEquals("20240327", result.getReleaseDate());
        assertEquals("109", result.getRunningTime());
        assertEquals("손석구, 김성철, 김동휘, 홍경, 김희원", result.getActors());
        assertEquals("안국진", result.getDirector());
    }
}
