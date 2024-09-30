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
    public DibEntity saveNewDib(UUID userId, DibEntity dib) {
        return null;
    }

    @Override
    public Optional<DibEntity> findByDibId(Long id) {
        return Optional.empty();
    }

    @Override
    public Long countMovie(String movieId) {
        return null;
    }

    @Override
    public List<DibEntity> selectAll() {
        return null;
    }

    @Override
    public void deleteDib(Long dibId) {

    }

    @Override
    public void deleteDib(UUID userId) {

    }
}
