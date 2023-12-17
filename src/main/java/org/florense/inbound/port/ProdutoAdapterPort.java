package org.florense.inbound.port;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.florense.inbound.adapter.dto.ProdutoDto;
import org.florense.inbound.adapter.dto.ProdutoDtoSimple;

import java.util.List;

public interface ProdutoAdapterPort {


    ProdutoDto updateProduto(@Valid ProdutoDto produtoDto);

    ProdutoDto createProdutoSearch(@Valid ProdutoDtoSimple simple);

    ProdutoDto updateProdutoSimple(@Valid ProdutoDtoSimple simple);

    ProdutoDto searchExitProd(@PathParam("mlId") String mlID);

    List<ProdutoDto> listAll();

    List<String> listAllActiveMl();

    List<String> listAllActiveMlMinusRegistered();

    ProdutoDto findProdutoByMlId(@PathParam("mlId") String mlId);

    void deleteById(@PathParam("id") Long id);
}
