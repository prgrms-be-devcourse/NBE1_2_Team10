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
    Long countMovie(String movieId);
    List<DibEntity> selectAll();
    int deleteDibByDibId(Long dibId);
    int deleteDibByUserId(UUID userId);
}
