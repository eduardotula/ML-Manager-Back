package org.florense.outbound.adapter.postgre.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity(name = "venda")
@Table(name = "venda")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantidade", columnDefinition = "integer")
    private int quantidade;
    @Column(name = "preco_desconto", columnDefinition = "NUMERIC(18,2)")
    private double precoDesconto;
    @Column(name = "taxa_ml", columnDefinition = "NUMERIC(18,2)")
    private double taxaML;
    @Column(name = "custo_frete", columnDefinition = "NUMERIC(18,2)")
    private double custoFrete;
    @Column(name = "custo", columnDefinition = "NUMERIC(18,2)")
    private double custo;
    @Column(name = "lucro", columnDefinition = "NUMERIC(18,2)")
    private double lucro;
    @Column(name = "completo")
    private boolean completo;
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "anuncio_id_fk")
    private AnuncioEntity anuncio;

    @ManyToOne
    @JoinColumn(name = "order_id_fk")
    private OrderEntity order;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
