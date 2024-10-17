package core.application.users.repositories;

import core.application.users.mapper.UserMapper;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 사용자 정보를 처리하는 {@link UserRepository}의 구현체
 * 이 클래스는 사용자 정보에 대한 CRUD 작업을 수행하며,
 * {@link UserMapper}를 사용하여 데이터베이스와 상호작용함
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper mapper;
    private final JpaUserRepository jpaRepository;


    @Override
    public UserEntity saveNewUser(UserEntity newUser) {
//        return mapper.saveNewUser(newUser);
        return jpaRepository.save(newUser);
    }

    @Override
    public Optional<UserEntity> findByUserId(UUID userId) {
//        return mapper.findByUserId(userId);
        return jpaRepository.findById(userId);
    }

    @Override
    public Boolean existsByEmail(String email) {
//        return mapper.findByUserEmail(email).isPresent();
        return jpaRepository.existsByUserEmail(email);
    }

    @Override
    public Optional<UserEntity> findByUserEmail(String email) {
//        return mapper.findByUserEmail(email);
        return jpaRepository.findByUserEmail(email);
    }

    @Override
    public Optional<UserEntity> findByUserEmailAndPassword(String userEmail, String userPw) {
//        return mapper.findByUserEmailAndPassword(userEmail, userPw);
        return jpaRepository.findByUserEmailAndUserPw(userEmail, userPw);
    }
    @Override
    public List<UserEntity> findByUserRole(UserRole role) {
//        return mapper.findByUserRole(role);
        return jpaRepository.findByRole(role);
    }

    @Override
    public List<UserEntity> findAll() {
//        return mapper.findAll();
        return jpaRepository.findAll();
    }

    @Override
    public Boolean existsByUserId(UUID userId) {
        return jpaRepository.existsById(userId);
    }

    @Override
    public UserEntity editUserInfo(UserEntity replacement) {
//        return mapper.editUserInfo(replacement);
        return jpaRepository.save(replacement);
    }

    @Override
    public void deleteUser(UUID userId) {
//        return mapper.deleteUser(userId);
        jpaRepository.deleteById(userId);
    }
}
