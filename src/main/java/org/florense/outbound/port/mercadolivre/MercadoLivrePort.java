package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Produto;

import java.util.List;

public interface MercadoLivrePort {
    Produto getProduto(String mlId);

    Double getTarifas(Double preco, String categoria);

    Double getFrete(String mlId, String cep);

    List<String> listActiveMlIds();
}
