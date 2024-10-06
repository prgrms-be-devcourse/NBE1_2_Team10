package core.application.users.repositories;

import core.application.users.models.entities.DibEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DibRepositoryImplTest {

    @Autowired
    private DibRepository dibRepo;

    @Test
    @DisplayName("찜하기")
    @Transactional
    void saveNewDib() {
        // Given
        UUID userId = UUID.fromString("b7172110-7e18-11ef-8da5-467268b55380");
        String movieId = "K-1234";
        dibRepo.saveNewDib(userId, movieId);

        // When
        Optional<DibEntity> find = dibRepo.findByUserIdAndMovieId(userId, movieId);
        System.out.println(find);

        // Then
        assertThat(find.isPresent());
        assertThat(find.get().getUserId() == UUID.fromString("b7172110-7e18-11ef-8da5-467268b55380"));
        assertThat(find.get().getMovieId().equals(movieId));
    }

    @Test
    @DisplayName("찜 아이디로 찜 찾기")
    @Transactional
    void findByDibId() {
        // Given
        UUID userId = UUID.fromString("b7172110-7e18-11ef-8da5-467268b55380");
        String movieId = "K-1234";

        dibRepo.saveNewDib(userId, movieId);
        Long dibId = dibRepo.findByUserIdAndMovieId(userId, movieId).get().getDibId();

        // When
        Optional<DibEntity> find = dibRepo.findByDibId(dibId);

        // Then
        assertThat(find.isPresent());
        assertThat(find.get().getDibId().equals(dibId));
        assertThat(find.get().getUserId() == userId);
        assertThat(find.get().getMovieId().equals(movieId));
    }

    @Test
    @DisplayName("특정 영화의 찜 개수 세기")
    @Transactional
    void countMovie() {
        // Given
        String movieId = "K-1111";
        UUID userId1 = UUID.fromString("9d8ae540-8072-11ef-8da5-467268b55380");
        UUID userId2 = UUID.fromString("9d8e127e-8072-11ef-8da5-467268b55380");

        dibRepo.saveNewDib(userId1, movieId);
        dibRepo.saveNewDib(userId2, movieId);

        // When
        Long num = dibRepo.countMovie(movieId);

        // Then
        assertThat(num == 2);
    }

    @Test
    @DisplayName("찜 객체 리스트 전체 조회")
    @Transactional
    void selectAll() {
        // Given
        UUID userId1 = UUID.fromString("9d8ae540-8072-11ef-8da5-467268b55380");
        UUID userId2 = UUID.fromString("9d8e127e-8072-11ef-8da5-467268b55380");
        String movieId = "K-1234";

        dibRepo.saveNewDib(userId1, movieId);
        dibRepo.saveNewDib(userId2, movieId);

        Long dibId1 = dibRepo.findByUserIdAndMovieId(userId1, movieId).get().getDibId();
        Long dibId2 = dibRepo.findByUserIdAndMovieId(userId2, movieId).get().getDibId();

        // When
        List<DibEntity> dibs = dibRepo.selectAll();

        // Then
        assertThat(dibs.get(0).getDibId().equals(dibId1));
        assertThat(dibs.get(0).getUserId() == userId1);
        assertThat(dibs.get(0).getMovieId().equals(movieId));

        assertThat(dibs.get(1).getDibId().equals(dibId2));
        assertThat(dibs.get(1).getUserId() == userId2);
        assertThat(dibs.get(1).getMovieId().equals(movieId));
    }

    @Test
    @DisplayName("찜 아이디로 찜 삭제하기")
    @Transactional
    void deleteDibByDibId() {
        // Given
        UUID userId = UUID.fromString("9d8ae540-8072-11ef-8da5-467268b55380");
        String movieId = "K-1111";
        dibRepo.saveNewDib(userId, movieId);

        // When
        Long dibId = dibRepo.findByUserIdAndMovieId(userId, movieId).get().getDibId();
        dibRepo.deleteDib(dibId);

        // Then
        assertThat(!dibRepo.findByDibId(dibId).isPresent());
    }

    @Test
    @DisplayName("유저 아이디로 찜 삭제하기")
    @Transactional
    void DeleteDibByUserId() {
        // Given
        UUID userId = UUID.fromString("9d8ae540-8072-11ef-8da5-467268b55380");
        String movieId = "K-1111";
        dibRepo.saveNewDib(userId, movieId);

        // When
        dibRepo.deleteDib(userId);

        // Then
        assertThat(!dibRepo.findByUserIdAndMovieId(userId, movieId).isPresent());
    }

    @Test
    @DisplayName("유저 아이디랑 영화 아이디로 찜 삭제하기")
    @Transactional
    void DeleteDibByUserIdAndMovieId() {
        // Given
        UUID userId = UUID.fromString("9d8ae540-8072-11ef-8da5-467268b55380");
        String movieId = "K-1111";
        dibRepo.saveNewDib(userId, movieId);

        // When
        dibRepo.deleteDib(userId, movieId);

        // Then
        assertThat(!dibRepo.findByUserIdAndMovieId(userId, movieId).isPresent());
    }
}