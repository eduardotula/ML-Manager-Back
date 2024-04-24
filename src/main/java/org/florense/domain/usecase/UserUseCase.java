package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.User;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.postgre.UserEntityPort;

import java.util.List;

@RequestScoped
public class UserUseCase {

    @Inject
    UserEntityPort port;

    public User create(User user) {
        if(user.getId() != null) throw new IllegalArgumentException(String.format("User com id: %s já registrado", user.getId()));
        user.setId(null);
        return port.createUpdate(user);
    }

    public User update(User user) {
        var existAccessCode = port.findById(user.getId());
        if(existAccessCode == null) throw new IllegalArgumentException(String.format("User com id: %d não encontrado", user.getId()));

        return port.createUpdate(user);
    }

    //Atualmente retorna o unico accessCode, metodo criado para possiveis implementações futuras em que pode haver mais de um accessCode e users
    public User get() throws FailRequestRefreshTokenException {
        var accesses = port.listAll();
        if(accesses.isEmpty()) throw new FailRequestRefreshTokenException("Falha ao obter AccessToken error: User não encontrado");
        return accesses.get(0);
    }

    public void deleteById(Long id){
        if(port.findById(id) == null) throw new IllegalArgumentException(String.format("User com id: %d não encontrado",id));
        port.deleteById(id);
    }

    public List<User> listALl(){
        return port.listAll();
    }
}
