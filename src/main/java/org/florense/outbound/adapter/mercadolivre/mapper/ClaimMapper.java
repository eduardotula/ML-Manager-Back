package org.florense.outbound.adapter.mercadolivre.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Reclamacao;
import org.florense.outbound.adapter.mercadolivre.response.MLClaimResponse;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface ClaimMapper {

    Reclamacao toModel(MLClaimResponse response);
}
