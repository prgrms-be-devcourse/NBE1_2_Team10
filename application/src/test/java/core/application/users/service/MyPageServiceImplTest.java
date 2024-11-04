package core.application.users.service;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.movie.CachedMovieRepository;
import core.application.users.models.dto.MyPageRespDTO;
import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.DibRepositoryImpl;
import core.application.users.repositories.UserRepositoryImpl;

@SpringBootTest
@Transactional
class MyPageServiceImplTest {

	@Autowired
	private MyPageServiceImpl myPageService;
	@Autowired
	private UserRepositoryImpl userRepo;
	@Autowired
	private CachedMovieRepository movieRepo;
	@Autowired
	private DibRepositoryImpl dibRepo;

	private static UserEntity testUser;
	private static DibEntity testDib;
	private static CachedMovieEntity testMovie;
	private static final UUID userId = UUID.fromString("991c95d6-808a-11ef-8da5-467268b55380");
	private static final String movieId = "K-1111";

	@BeforeAll
	static void init() {

		testUser = UserEntity.builder()
			.userEmail("test@test.com")
			.userPw("test")
			.role(UserRole.USER)
			.alias("소은")
			.phoneNum("010-0000-0000")
			.userName("정소은")
			.userId(userId)
			.build();

		testDib = DibEntity.builder()
			.userId(userId)
			.movieId(movieId)
			.build();

		testMovie = CachedMovieEntity.builder()
			.movieId(movieId)
			.title("제목1")
			.posterUrl("poster.jpg")
			.genre("로맨스")
			.plot("줄거리")
			.releaseDate("2024-05-12")
			.runningTime("60")
			.actors("마동석")
			.director("봉준호")
			.dibCount(1L)
			.reviewCount(1L)
			.commentCount(1L)
			.sumOfRating(4L)
			.build();
	}

	@Test
	@DisplayName("마이페이지 조회하기")
	void getMyPage() {
		// Given
		userRepo.saveNewUser(testUser);
		movieRepo.saveNewMovie(testMovie);
		dibRepo.saveNewDib(userId, testDib.getMovieId());

		// When
		MyPageRespDTO myPageRespDTO = myPageService.getMyPage(userId);

		// Then
		assertThat(myPageRespDTO.getUserEmail().equals(testUser.getUserEmail()));
		assertThat(myPageRespDTO.getAlias().equals(testUser.getAlias()));
		assertThat(myPageRespDTO.getUserName().equals(testUser.getUserName()));
		assertThat(myPageRespDTO.getRole().equals(testUser.getRole()));
		assertThat(myPageRespDTO.getPhoneNum().equals(testUser.getPhoneNum()));

		assertThat(myPageRespDTO.getDibDTOList().get(0).getMovieId().equals(testMovie.getMovieId()));
		assertThat(myPageRespDTO.getDibDTOList().get(0).getMovieTitle().equals(testMovie.getTitle()));
		assertThat(myPageRespDTO.getDibDTOList().get(0).getMoviePost().equals(testMovie.getPosterUrl()));
	}
}
