package core.application.users.repositories;

import core.application.users.mapper.DibMapper;
import core.application.users.models.entities.DibEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DibRepositoryImpl implements DibRepository{

    private final DibMapper mapper;

    @Override
    public int saveNewDib(UUID userId, DibEntity dib) {
        return mapper.saveNewDib(userId, dib);
    }

    @Override
    public Optional<DibEntity> findByDibId(Long id) {
        return mapper.findByDibId(id);
    }

    @Override
    public List<DibEntity> findByUserId(UUID userId) { return mapper.findByUserId(userId); }

    @Override
    public Long countMovie(String movieId) {
        return mapper.countMovie(movieId);
    }

    @Override
    public List<DibEntity> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public void deleteDib(Long dibId) {
        mapper.deleteDibByDibId(dibId);
    }

    @Override
    public void deleteDib(UUID userId) {
        mapper.deleteDibByUserId(userId);
    }

    @Override
    public void deleteDib(UUID userId, String movieId) {
        mapper.deleteBidByUserIdAndMovieId(userId, movieId);
    }
}
