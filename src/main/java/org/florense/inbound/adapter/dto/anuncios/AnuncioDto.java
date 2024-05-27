package org.florense.inbound.adapter.dto.anuncios;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.florense.domain.model.enums.ListingTypeEnum;
import org.florense.inbound.adapter.dto.UrlDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnuncioDto {

    private Long id;
    @NotNull
    private String mlId;
    @NotNull()
    private String sku;
    @NotNull
    private String gtin;
    @NotNull
    private String url;
    @NotNull
    private String descricao;
    @NotNull
    private String categoria;
    @NotNull
    private double custo;
    @NotNull
    @Pattern(regexp = "102|500",message = "campo tipo do anuncio, valores: 102, 500", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String csosn;
    @NotNull
    private double precoDesconto;
    @NotNull
    private double taxaML;
    @NotNull
    private double custoFrete;
    @NotNull
    private String status;
    @NotNull
    private LocalDateTime createdAt;
    private double imposto;
    private ListingTypeEnum listingType;
    private double lucro;
    private List<UrlDto> pictures;
    private boolean complete;
    private boolean fulfillment;
    private boolean catalogListing;

}
