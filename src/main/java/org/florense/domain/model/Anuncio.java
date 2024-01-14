package org.florense.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Anuncio {

    private Long id;
    private String mlId;
    private String sku;
    private String gtin;
    private String url;
    private String descricao;
    private String categoria;
    private double custo;
    private String csosn;
    private double precoDesconto;
    private double taxaML;
    private double custoFrete;
    private String status;
    private LocalDateTime createdAt;
    private double lucro;
    private ListingTypeEnum listingType;
    private User user;

    public void update(Anuncio anuncio){
        this.id = anuncio.getId();
        this.createdAt = anuncio.getCreatedAt();
    }

    public static double calculateLucro(Anuncio anuncio){
        BigDecimal porcenNf = new BigDecimal(6);
        if(anuncio.getCsosn().equals("102")) new BigDecimal(11);
        porcenNf = porcenNf.setScale(2,  RoundingMode.HALF_UP);
        porcenNf = porcenNf.divide(new BigDecimal(100), RoundingMode.HALF_UP);

        var precoDesconto = BigDecimal.valueOf(anuncio.getPrecoDesconto()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal nfTaxa = precoDesconto.multiply(porcenNf);
        double custoTotal = anuncio.getCusto() + anuncio.getTaxaML() + anuncio.getCustoFrete() + nfTaxa.doubleValue();
        var lucroBig = new BigDecimal(anuncio.getPrecoDesconto() - custoTotal);
        lucroBig = lucroBig.setScale(2,RoundingMode.HALF_UP);
        return lucroBig.doubleValue();
    }
}
