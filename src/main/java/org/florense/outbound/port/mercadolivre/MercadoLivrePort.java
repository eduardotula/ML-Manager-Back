package org.florense.outbound.port.mercadolivre;

import org.florense.domain.model.Produto;
import org.florense.outbound.adapter.mercadolivre.exceptions.UnauthorizedAcessKeyException;

import java.util.List;

public interface MercadoLivrePort {
    Produto getProduto(String mlId);

    Double getTarifas(Double preco, String categoria);

    Double getFrete(String mlId, String cep);

    List<String> listActiveMlIds() ;
}
