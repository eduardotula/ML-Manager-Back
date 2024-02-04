package org.florense.domain.model;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Venda {

    private Long id;
    private int quantidade;
    private double precoDesconto;
    private double taxaML;
    private double custoFrete;
    private double custo;
    private double lucro;
    private boolean completo;
    private String status;
    private Long orderId;
    private Anuncio anuncio;
    private LocalDateTime createdAt;

    public void setDataFromAnuncio(Anuncio anuncio){
        this.taxaML = anuncio.getTaxaML();
        this.custoFrete = anuncio.getCustoFrete();
        this.custo = anuncio.getCusto();
        this.lucro = Anuncio.calculateLucro(anuncio);
    }
}
