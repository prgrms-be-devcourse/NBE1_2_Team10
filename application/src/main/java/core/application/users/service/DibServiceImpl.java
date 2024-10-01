package core.application.users.service;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.CachedMovieRepository;
import core.application.users.models.dto.DibRespDTO;
import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.repositories.DibRepository;
import core.application.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DibServiceImpl implements DibService {

    private final DibRepository dibRepo;
    private final CachedMovieRepository movieRepo;
    private final UserRepository userRepo;

    // userId 파라미터 제거해야 함 -> interface도
    @Override
    public DibRespDTO createDib(UUID userId, String movieId) {
        // 현재 유저 불러오기 -> 나중에 로직 수정
        Optional<UserEntity> user = userRepo.findByUserId(userId);
        Optional<CachedMovieEntity> movie = movieRepo.findByMovieId(movieId);


        // Dib Entity 생성
        DibEntity dib = DibEntity.builder()
                .userId(userId)
                .movieId(movieId)
                .build();

        dibRepo.saveNewDib(userId, dib);

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

    // userId 파라미터 제거해야 함 -> interface도
    @Override
    public DibRespDTO cancelDib(UUID userId, Long dibId) {
        // 현재 유저 불러오기
        Optional<UserEntity> user = userRepo.findByUserId(userId);

        // 찜 취소할 영화 아이디
        String movieId = dibRepo.findByDibId(dibId).get().getMovieId();
        Optional<CachedMovieEntity> movie = movieRepo.findByMovieId(movieId);

        // 찜 레코드 삭제
        dibRepo.deleteDib(dibId);

        // dib_count 1 감소하는 로직 추가
        movie.get().decrementDibCount();
        movieRepo.editMovie(movieId, movie.get());

        DibRespDTO dibDTO = DibRespDTO.builder()
                .message("찜 취소 완료되습니다.")
                .userId(userId)
                .movieId(movieId)
                .build();

        return dibDTO;
    }
}
