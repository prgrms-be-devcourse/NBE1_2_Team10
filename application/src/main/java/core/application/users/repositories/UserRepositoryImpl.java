package core.application.users.repositories;

import core.application.users.mapper.UserMapper;
import core.application.users.models.entities.DibEntity;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final UserMapper mapper;


    @Override
    public int saveNewUser(UserEntity newUser) {
        return mapper.saveNewUser(newUser);
    }

    @Override
    public Optional<UserEntity> findByUserId(UUID userId) {
        return mapper.findByUserId(userId);
    }

    @Override
    public Optional<UserEntity> findByUserEmail(String email) {
        return mapper.findByUserEmail(email);
    }

    @Override
    public Optional<UserEntity> findByUserEmailAndPassword(String email, String password) {
        return mapper.findByUserEmailAndPassword(email, password);
    }
    @Override
    public List<UserEntity> findByUserRole(UserRole role) {
        return mapper.findByUserRole(role);
    }

    @Override
    public List<DibEntity> selectDibsOnUserId(UUID userId) {
        return mapper.selectDibsOnUserId(userId);
    }

    @Override
    public List<UserEntity> findAll() {
        return mapper.findAll();
    }

    @Override
    public int editUserInfo(UserEntity replacement) {
        return mapper.editUserInfo(replacement);
    }

    @Override
    public int deleteUser(UUID userId) {
        return mapper.deleteUser(userId);
    }
}
