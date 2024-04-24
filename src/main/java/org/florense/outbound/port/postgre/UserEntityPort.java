package org.florense.outbound.port.postgre;

import org.florense.domain.model.User;

import java.util.List;

public interface UserEntityPort {

    User createUpdate(User user);

    User findById(Long id);

    User findByMlIdUser(String mlUserId);

    List<User> listAll();

    void deleteById(Long id);
}
