package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Produto;

public interface MercadoLivrePort {
    Produto getProduto(String mlId);
}
