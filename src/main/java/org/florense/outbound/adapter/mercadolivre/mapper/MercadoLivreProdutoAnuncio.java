package org.florense.outbound.adapter.mercadolivre.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.ListingTypeEnum;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreAnuncioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface MercadoLivreProdutoAnuncio {

    @Mapping(source = "title", target = "descricao")
    @Mapping(source = "permalink", target = "url")
    @Mapping(source = "category_id", target = "categoria")
    @Mapping(source = "price", target = "precoDesconto")
    @Mapping(source = "listing_type_id", target = "listingType", qualifiedByName = "mapListingType")
    Anuncio toAnuncio(MercadoLivreAnuncioResponse ml);

    @Named("mapListingType")
    default ListingTypeEnum mapListingType(String listingType) {
        if(listingType.equals(ListingTypeEnum.premium.getValue())) return ListingTypeEnum.premium;
        return ListingTypeEnum.classico;
    }
}
