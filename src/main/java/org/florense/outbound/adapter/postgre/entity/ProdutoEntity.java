package org.florense.outbound.adapter.postgre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "produtos")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ml_id")
    private String mlId;
    @Column(name = "sku", nullable = false)
    private String sku;
    @Column(name = "gtin")
    private String gtin;
    @Column(name = "url")
    private String url;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "custo", nullable = false)
    private double custo;
    @Column(name = "csosn")
    private String csosn;
    @Column(name = "preco_desconto")
    private double precoDesconto;
    @Column(name = "taxa_ml")
    private double taxaML;
    @Column(name = "custo_frete")
    private double custoFrete;
    @Column(name = "ativo", nullable = false)
    private boolean ativo;


}
