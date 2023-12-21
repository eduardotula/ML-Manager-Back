package org.florense.inbound.port;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.florense.domain.model.Produto;
import org.florense.inbound.adapter.dto.ProdutoDto;
import org.florense.inbound.adapter.dto.ProdutoDtoSimple;
import org.florense.outbound.adapter.mercadolivre.exceptions.FailRequestRefreshTokenException;

import java.util.List;

public interface ProdutoAdapterPort {


    Produto createUpdate(Produto produto);

    //Cria e atualiza com mercado livre
    Produto createMlSearch(Produto produto) throws FailRequestRefreshTokenException;

    //Somente atualiza um produto
    Produto updateSimple(Produto produto);

    //Atualiza dados somente do mercado livre
    Produto updateSearch(String mlId) throws FailRequestRefreshTokenException;

    List<String> listAllActiveMl() throws FailRequestRefreshTokenException;

    List<String> listAllActiveMlMinusRegistered() throws FailRequestRefreshTokenException;

    Produto findProdutoByMlId(String mlId);

    @Transactional
    Produto findProdutoByMlIdSearch(String mlId) throws FailRequestRefreshTokenException;

    List<Produto> listAll();

    void deleteBy(Long id);
}
