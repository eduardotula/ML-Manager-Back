package org.florense.outbound.adapter.mercadolivre.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.florense.domain.model.Anuncio;
import org.florense.domain.model.enums.ListingTypeEnum;
import org.florense.domain.model.Url;
import org.florense.outbound.adapter.mercadolivre.response.MercadoLivreAnuncioResponse;
import org.mapstruct.*;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface MercadoLivreProdutoAnuncio {

    @Mapping(source = "title", target = "descricao")
    @Mapping(source = "permalink", target = "url")
    @Mapping(source = "category_id", target = "categoria")
    @Mapping(source = "price", target = "precoDesconto")
    @Mapping(source = "listing_type_id", target = "listingType", qualifiedByName = "mapListingType")
    @Mapping(source = "pictures", target = "pictures", qualifiedByName = "urlsMapping")
    Anuncio toAnuncio(MercadoLivreAnuncioResponse ml);

    @Named("mapListingType")
    default ListingTypeEnum mapListingType(String listingType) {
        if(listingType.equals(ListingTypeEnum.premium.getValue())) return ListingTypeEnum.premium;
        return ListingTypeEnum.classico;
    }

    @Named("urlsMapping")
    default List<Url> pictures(List<String> pictures) {
        List<Url> urls = new LinkedList<>();
        pictures.forEach(url -> urls.add(new Url(null, url, 0)));
        return urls;
    }

}
