package org.r2learning.user.infrastructure.db.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.r2learning.user.domain.user.User;
import org.r2learning.user.domain.user.gateway.UserGateway;
import org.r2learning.user.infrastructure.db.dataobject.UserDO;
import org.r2learning.user.infrastructure.db.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserGateway {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        UserDO userDO = UserMapper.INSTANCE.userToDO(user);
        UserDO savedDO = jpaUserRepository.save(userDO);
        return UserMapper.INSTANCE.userToEntity(savedDO);
    }

    @Override
    public User findById(Long id) {
        return jpaUserRepository.findById(id)
            .map(UserMapper.INSTANCE::userToEntity)
            .orElse(null);
    }

    @Override
    public void delete(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
            .map(UserMapper.INSTANCE::userToEntity)
            .toList();
    }

    @Override
    public List<User> findByTeamId(Long teamId) {
        return jpaUserRepository.findByTeamId(teamId).stream()
            .map(UserMapper.INSTANCE::userToEntity)
            .toList();
    }

    @Override
    public List<User> findByIds(List<Long> userIds) {
        return jpaUserRepository.findByIdIn(userIds).stream()
            .map(UserMapper.INSTANCE::userToEntity)
            .toList();
    }

    @Override
    public User findByEmail(String email) {
        UserDO userDO = jpaUserRepository.findByEmail(email);
        return userDO != null ? UserMapper.INSTANCE.userToEntity(userDO) : null;
    }
}

