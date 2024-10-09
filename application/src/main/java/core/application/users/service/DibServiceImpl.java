package core.application.users.service;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.movie.CachedMovieRepository;
import core.application.users.models.dto.DibRespDTO;
import core.application.users.repositories.DibRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DibServiceImpl implements DibService {

    private final DibRepository dibRepo;
    private final CachedMovieRepository movieRepo;

    @Override
    public DibRespDTO dibProcess(UUID userId, String movieId) {

        // 찜 취소 혹은 생성할 영화 객체
        Optional<CachedMovieEntity> movie = movieRepo.findByMovieId(movieId);

        // dib_table에 이미 존재하는 객체 -> 찜 취소하기
        if(dibRepo.findByUserIdAndMovieId(userId, movieId).isPresent()) {
            // 찜 레코드 삭제
            dibRepo.deleteDib(userId, movieId);

            // dib_count 1 감소하는 로직 추가
            movie.get().decrementDibCount();
            movieRepo.editMovie(movieId, movie.get());

            DibRespDTO dibDTO = DibRespDTO.builder()
                    .message("찜 취소 완료되습니다.")
                    .userId(userId)
                    .movieId(movieId)
                    .build();
            return dibDTO;

        } else {
            dibRepo.saveNewDib(userId, movieId);

            // dib_count 1 증가하는 로직 추가
            movie.get().incrementDibCount();
            movieRepo.editMovie(movieId, movie.get());

            DibRespDTO dibDTO = DibRespDTO.builder()
                    .message("찜 완료되었습니다.")
                    .userId(userId)
                    .movieId(movieId)
                    .build();

            return dibDTO;
        }
    }
}
