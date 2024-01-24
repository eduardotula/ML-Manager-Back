package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.Url;
import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;
import org.florense.outbound.adapter.postgre.entity.UrlEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
@Mapper(componentModel = "jakarta", uses = {VendaEntityMapper.class})
public interface AnuncioEntityMapper {

    Anuncio toModel(AnuncioEntity anuncio);

    AnuncioEntity toEntity(Anuncio anuncio);

    @AfterMapping
    default void setSetAnuncioPictures(@MappingTarget Anuncio anuncio) {
        anuncio.getPictures().forEach(picture -> picture.setAnuncioId(anuncio.getId()));
    }

}
