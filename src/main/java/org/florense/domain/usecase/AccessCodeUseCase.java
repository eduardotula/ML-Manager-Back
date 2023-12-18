package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.florense.domain.model.AccessCode;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;
import org.florense.outbound.port.postgre.AccessCodePort;

import java.util.List;

@RequestScoped
public class AccessCodeUseCase {

    @Inject
    AccessCodePort port;

    @Transactional
    public AccessCode create(AccessCode accessCode) {
        if(accessCode.getId() != null) throw new IllegalArgumentException(String.format("AccessCode com id: %s já registrado",accessCode.getId()));
        accessCode.setId(null);
        return port.createUpdate(accessCode);
    }

    @Transactional
    public AccessCode update(AccessCode accessCode) {
        var existAccessCode = port.findById(accessCode.getId());
        if(existAccessCode == null) throw new IllegalArgumentException(String.format("AccessCode com id: %s não encontrado",accessCode.getId()));

        return port.createUpdate(accessCode);
    }

    @Transactional
    //Atualmente retorna o unico accessCode, metodo criado para possiveis implementações futuras em que pode haver mais de um accessCode e users
    public AccessCode get() throws FailRequestRefreshTokenException {
        var accesses = port.listAll();
        if(accesses.isEmpty()) throw new FailRequestRefreshTokenException("Falha ao obter AccessToken error: AccessCode não encontrado");
        return accesses.get(0);
    }

    @Transactional
    public void deleteById(Long id){
        if(port.findById(id) == null) throw new IllegalArgumentException(String.format("AccessCode com id: %s não encontrado",id));
        port.deleteById(id);
    }

    @Transactional
    public List<AccessCode> listALl(){
        return port.listAll();
    }
}
