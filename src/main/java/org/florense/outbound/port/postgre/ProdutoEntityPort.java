package org.florense.outbound.port.postgre;

import org.florense.domain.model.Produto;

import java.util.List;

public interface ProdutoEntityPort {
    Produto saveUpdate(Produto produto);

    Produto findById(Long id);

    Produto findByMlId(String mlId);

    List<Produto> listAll();

    void deleteById(Long id);
}
