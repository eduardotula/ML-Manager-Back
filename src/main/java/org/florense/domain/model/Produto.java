package org.florense.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Produto {

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

    public void update(Produto produto){
        this.id = produto.getId();
        this.createdAt = produto.getCreatedAt();
    }
}
