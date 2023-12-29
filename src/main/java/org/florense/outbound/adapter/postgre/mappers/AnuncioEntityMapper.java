package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.mapstruct.Mapper;

@ApplicationScoped
@Mapper(componentModel = "jakarta", uses = VendaEntityMapper.class)
public interface AnuncioEntityMapper {

    Anuncio toModel(AnuncioEntity anuncio);
    AnuncioEntity toEntity(Anuncio anuncio);
}
