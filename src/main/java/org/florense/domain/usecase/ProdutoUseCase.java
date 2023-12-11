package org.florense.domain.usecase;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.florense.domain.model.Produto;
import org.florense.outbound.port.postgre.ProdutoEntityPort;

@RequestScoped
public class ProdutoUseCase {

    @Inject
    ProdutoEntityPort produtoEntityPort;

    public Produto saveUpdate(Produto produto){
        return produtoEntityPort.saveUpdate(produto);

    }
}
