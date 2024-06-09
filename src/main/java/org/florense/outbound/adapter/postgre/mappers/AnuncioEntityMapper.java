package org.florense.outbound.adapter.postgre.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    @Mapping(source = ".", target = "lucro", qualifiedByName = "calculateLucro")
    Anuncio toModel(AnuncioEntity anuncio);

    AnuncioEntity toEntity(Anuncio anuncio);

    @AfterMapping
    default void afterMappingEntity(@MappingTarget AnuncioEntity anuncio){
        anuncio.getPictures().forEach(pic -> pic.setAnuncio(anuncio));
    }

    @Named("calculateLucro")
    default double calculateLucro(AnuncioEntity anuncio){
        double imposto = Anuncio.calculateImposto(anuncio.getCsosn(), anuncio.getPrecoDesconto());
        return Anuncio.calculateLucro(anuncio.getCusto(), anuncio.getTaxaML(), anuncio.getCustoFrete(), imposto, anuncio.getPrecoDesconto());
    }

}
