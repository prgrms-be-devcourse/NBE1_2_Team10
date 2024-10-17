package core.application.users.repositories;

import core.application.users.mapper.DibMapper;
import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DibRepositoryImpl implements DibRepository{

    private final DibMapper mapper;
    private final JpaDibRepository jpaRepository;

    @Override
    public DibEntity saveNewDib(DibEntity dib) {
//        mapper.saveNewDib(userId, movieId);
//        return mapper.findByUserIdAndMovieId(userId, movieId);
        return jpaRepository.save(dib);
    }

    @Override
    public Optional<DibEntity> findByDibId(Long id) {
//        return mapper.findByDibId(id);
        return jpaRepository.findById(id);
    }

    @Override

    public List<DibEntity> findByUserId(UUID userId) { return jpaRepository.findByUser_UserId(userId); }

    public Optional<DibEntity> findByUserIdAndMovieId(UUID userId, String movieId) { return jpaRepository.findByUser_UserIdAndMovie_MovieId(userId, movieId); }


    @Override
    public Long countMovie(String movieId) {
        return mapper.countMovie(movieId);
    }

    @Override
    public List<DibEntity> selectAll() {
//        return mapper.selectAll();
        return jpaRepository.findAll();
    }

    @Override
    public void deleteDib(Long dibId) {
//        mapper.deleteDibByDibId(dibId);
        jpaRepository.deleteById(dibId);
    }

    @Override
    public void deleteDib(UUID userId) {
//        mapper.deleteDibByUserId(userId);
        jpaRepository.deleteByUser_UserId(userId);
    }

    @Override
    public void deleteDib(UUID userId, String movieId) {
//        mapper.deleteBidByUserIdAndMovieId(userId, movieId);
        jpaRepository.deleteByUser_UserIdAndMovie_MovieId(userId, movieId);
    }
}
