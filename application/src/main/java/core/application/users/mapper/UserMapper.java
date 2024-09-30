package core.application.users.mapper;

import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface UserMapper {
    int saveNewUser(UserEntity newUser);
    Optional<UserEntity> findByUserId(UUID userId);
    Optional<UserEntity> findByUserEmail(String email);
    Optional<UserEntity> findByUserEmailAndPassword(String email, String password);
    List<UserEntity> findByUserRole(UserRole role);
    List<DibEntity> selectDibsOnUserId(UUID userId);

    List<UserEntity> findAll();
    int editUserInfo(UUID userId, UserEntity replacement);
    int deleteUser(UUID userId);

}
