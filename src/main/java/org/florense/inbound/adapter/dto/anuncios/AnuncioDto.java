package org.florense.inbound.adapter.dto.anuncios;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.florense.domain.model.ListingTypeEnum;
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
}
