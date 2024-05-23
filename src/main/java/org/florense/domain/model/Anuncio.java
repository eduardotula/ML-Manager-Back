package org.florense.domain.model;

import lombok.*;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.florense.domain.model.enums.ListingTypeEnum;
import org.florense.domain.usecase.AnuncioUseCase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

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
    private double imposto;
    private boolean complete = false;
    private List<Url> pictures;
    private boolean isFulfillment;
    private boolean catalogListing;

    public Anuncio(Long id, String mlId, String sku, String gtin, String url, String descricao,
                   String categoria, double custo, String csosn, double precoDesconto, double taxaML,
                   double custoFrete, String status, LocalDateTime createdAt,
                   ListingTypeEnum listingType, User user, boolean complete, List<Url> pictures) {
        this.id = id;
        this.mlId = mlId;
        this.sku = sku;
        this.gtin = gtin;
        this.url = url;
        this.descricao = descricao;
        this.categoria = categoria;
        this.custo = custo;
        this.csosn = csosn;
        this.precoDesconto = precoDesconto;
        this.taxaML = taxaML;
        this.custoFrete = custoFrete;
        this.status = status;
        this.createdAt = createdAt;
        this.lucro = calculateLucro(custo, taxaML, custoFrete, imposto, precoDesconto);
        this.listingType = listingType;
        this.user = user;
        this.imposto = calculateImposto(this.csosn, this.precoDesconto);
        this.complete = complete;
        this.pictures = pictures;
    }

    public void update(Anuncio oldAnucio){
        this.id = oldAnucio.getId();
        this.createdAt = oldAnucio.getCreatedAt();
        this.user = oldAnucio.getUser();
    }

    public void updateLocalData(Anuncio oldAnuncio){
        this.custo = oldAnuncio.getCusto();
        this.csosn = oldAnuncio.getCsosn();
        this.lucro = calculateLucro(this);
    }

    public static double calculateLucro(double custo, double taxaML, double custoFrete, double imposto, double precoDesconto){

        if (precoDesconto <= ConfigProvider.getConfig().getValue("mercado-livre.maxpriceforfrete",Double.class)) custoFrete = 0.0;
        BigDecimal nfTaxa = new BigDecimal(Double.toString(imposto));
        double custoTotal = custo + taxaML + custoFrete + nfTaxa.doubleValue();
        var lucroBig = new BigDecimal(precoDesconto - custoTotal);
        lucroBig = lucroBig.setScale(2,RoundingMode.HALF_UP);
        return lucroBig.doubleValue();
    }

    public static double calculateLucro(Anuncio anuncio){
        anuncio.setImposto(Anuncio.calculateImposto(anuncio.csosn, anuncio.precoDesconto));
        return Anuncio.calculateLucro(anuncio.custo, anuncio.taxaML, anuncio.custoFrete, anuncio.imposto, anuncio.precoDesconto);
    }

    public static double calculateImposto(String csosn, double precoDesconto){
        BigDecimal porcenNf = new BigDecimal("6.92");
        if("102".equals(csosn)) porcenNf = new BigDecimal("10.40");
        porcenNf = porcenNf.setScale(4,  RoundingMode.HALF_UP);
        porcenNf = porcenNf.divide(new BigDecimal(100), RoundingMode.HALF_UP);

        var precoDescontoBig = BigDecimal.valueOf(precoDesconto).setScale(3, RoundingMode.HALF_UP);
        BigDecimal nfTaxa = precoDescontoBig.multiply(porcenNf);
        nfTaxa = nfTaxa.setScale(2,RoundingMode.HALF_UP);
        return nfTaxa.doubleValue();
    }

}
