package org.florense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private boolean ativo;
    private LocalDateTime createAt;
}
