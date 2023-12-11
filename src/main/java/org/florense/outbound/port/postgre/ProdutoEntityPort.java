package org.florense.outbound.port.postgre;

import org.florense.domain.model.Produto;

public interface ProdutoEntityPort {
    Produto saveUpdate(Produto produto);
}
