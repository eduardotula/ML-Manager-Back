package org.florense.outbound.adapter.postgre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.florense.domain.model.enums.ListingTypeEnum;

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
    @Column(name = "ml_id")
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
    @Column(name = "imposto", columnDefinition = "NUMERIC(18,2)")
    private double imposto;
    @Column(name = "preco_desconto", columnDefinition = "NUMERIC(18,2)")
    private double precoDesconto;
    @Column(name = "avaliable_quantity", columnDefinition = "integer")
    private int avaliableQuantity;
    @Column(name = "taxa_ml", columnDefinition = "NUMERIC(18,2)")
    private double taxaML;
    @Column(name = "custo_frete", columnDefinition = "NUMERIC(18,2)")
    private double custoFrete;
    @Column(name = "status")
    private String status;
    @Column(name = "listing_type")
    private ListingTypeEnum listingType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private UserEntity user;

    @OneToMany(mappedBy = "anuncio",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UrlEntity> pictures;
    @Column(name = "thumbnail_url", columnDefinition = "VARCHAR(255)")
    private String thumbnailUrl;
    @Column(name = "complete")
    private boolean complete;
    @Column(name = "is_fulfillment")
    private boolean fulfillment;
    @Column(name = "catalog_listing")
    private boolean catalogListing = false;
    @Column(name = "catalog_product_id")
    private String catalogProductId;


}
