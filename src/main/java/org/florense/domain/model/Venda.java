package org.florense.domain.model;


import lombok.*;

import java.math.BigDecimal;
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
    private double imposto;
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

    public void setCustoTotal(double custoUnico){
        this.custo = (double) Math.round((custoUnico * this.quantidade) * 100) / 100;
    }

    public void setCustoFreteTotal(double custoFreteUnico){
        this.custoFrete = (double) Math.round((custoFreteUnico * this.quantidade) * 100) / 100;
    }
    public void setTaxaMLTotal(double taxaMLUnico){
        this.taxaML = (double) Math.round((taxaMLUnico * this.quantidade) * 100) / 100;
    }
    public void setImpostoTotal(double impostoUnico){
        this.imposto = (double) Math.round((impostoUnico * this.quantidade) * 100) / 100;
    }
    public void setLucroTotal(double lucroUnico){
        this.lucro = (double) Math.round((lucroUnico * this.quantidade) * 100) / 100;
    }

}
