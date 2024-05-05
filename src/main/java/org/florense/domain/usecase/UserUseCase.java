package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.postgre.UserEntityPort;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;

@RequestScoped
public class UserUseCase {

    @Inject
    UserEntityPort port;
    @Inject
    Logger logger;

    public User create(User user) {
        logger.infof("Inicio createUser: name %s mlUserId %d", user.getName(), user.getUserIdML());

        if(Objects.nonNull(port.findById(user.getId()))) throw new IllegalArgumentException(String.format("User com id: %s já registrado", user.getId()));
        if(Objects.nonNull(port.findByMlIdUser(user.getUserIdML()))) throw new IllegalArgumentException(String.format("User com mlId: %s já registrado", user.getUserIdML()));

        user.setId(null);

        var returnUser = port.createUpdate(user);
        logger.infof("Final createUser: name %s mlUserId %d", user.getName(), user.getUserIdML());
        return returnUser;
    }

    public User update(User user) {
        logger.infof("Inicio updateUser: name %s mlUserId %d", user.getName(), user.getUserIdML());
        var existingUser = port.findById(user.getId());
        if(existingUser == null) throw new IllegalArgumentException(String.format("User com id: %d não encontrado", user.getId()));

        var returnUser = port.createUpdate(user);
        logger.infof("Final createUser: name %s mlUserId %d", user.getName(), user.getUserIdML());
        return returnUser;
    }

    public void deleteById(Long id){
        logger.infof("Inicio deleteById: id %d", id);
        if(port.findById(id) == null) throw new IllegalArgumentException(String.format("User com id: %d não encontrado",id));
        port.deleteById(id);

        logger.infof("Final deleteById: id %d", id);
    }

    public List<User> listALl(){
        return port.listAll();
    }
}
