package org.florense.domain.model;

import lombok.*;

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

    private List<Venda> vendas;

    public void update(Anuncio anuncio){
        this.id = anuncio.getId();
        this.createdAt = anuncio.getCreatedAt();
    }
}
