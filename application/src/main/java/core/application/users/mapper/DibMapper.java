package core.application.users.mapper;

import core.application.users.models.entities.DibEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface DibMapper {
    int saveNewDib(UUID userId, DibEntity dib);
    Optional<DibEntity> findByDibId(Long id);
    List<DibEntity> findByUserId(UUID userId);
    Long countMovie(String movieId);
    List<DibEntity> selectAll();
    void deleteDibByDibId(Long dibId);
    void deleteDibByUserId(UUID userId);
    void deleteBidByUserIdAndMovieId(UUID userId, String movieId);
}
