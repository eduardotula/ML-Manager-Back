package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.postgre.UserEntityPort;

import java.util.List;
import java.util.Objects;

@RequestScoped
public class UserUseCase {

    @Inject
    UserEntityPort port;

    public User create(User user) {
        if(Objects.nonNull(port.findById(user.getId()))) throw new IllegalArgumentException(String.format("User com id: %s já registrado", user.getId()));
        if(Objects.nonNull(port.findByMlIdUser(user.getUserIdML()))) throw new IllegalArgumentException(String.format("User com mlId: %s já registrado", user.getUserIdML()));

        user.setId(null);
        return port.createUpdate(user);
    }

    public User update(User user) {
        var existingUser = port.findById(user.getId());
        if(existingUser == null) throw new IllegalArgumentException(String.format("User com id: %d não encontrado", user.getId()));

        return port.createUpdate(user);
    }

    public void deleteById(Long id){
        if(port.findById(id) == null) throw new IllegalArgumentException(String.format("User com id: %d não encontrado",id));
        port.deleteById(id);
    }

    public List<User> listALl(){
        return port.listAll();
    }
}
