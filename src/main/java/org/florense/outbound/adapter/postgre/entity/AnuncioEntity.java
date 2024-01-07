package org.florense.outbound.adapter.postgre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.florense.domain.model.ListingTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "anuncio")
@Table(name = "anuncio")
public class AnuncioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ml_id", unique = true)
    private String mlId;
    @Column(name = "sku")
    private String sku;
    @Column(name = "gtin")
    private String gtin;
    @Column(name = "url")
    private String url;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "custo", columnDefinition = "NUMERIC(18,2)")
    private double custo;
    @Column(name = "csosn")
    private String csosn;
    @Column(name = "preco_desconto", columnDefinition = "NUMERIC(18,2)")
    private double precoDesconto;
    @Column(name = "taxa_ml", columnDefinition = "NUMERIC(18,2)")
    private double taxaML;
    @Column(name = "custo_frete", columnDefinition = "NUMERIC(18,2)")
    private double custoFrete;
    @Column(name = "lucro", columnDefinition = "NUMERIC(18,2)")
    private double lucro;
    @Column(name = "status")
    private String status;
    @Column(name = "listing_type")
    private ListingTypeEnum listingType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
