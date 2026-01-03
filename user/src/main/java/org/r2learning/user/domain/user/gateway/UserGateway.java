package org.r2learning.user.domain.user.gateway;

import java.util.List;
import org.r2learning.user.domain.user.User;

public interface UserGateway {
    User save(User user);

    User findById(Long id);

    void delete(Long id);

    List<User> findAll();

    List<User> findByTeamId(Long teamId);

    List<User> findByIds(List<Long> userIds);

    User findByEmail(String email);
}

