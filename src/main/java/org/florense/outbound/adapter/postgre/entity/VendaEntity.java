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
@Table(name = "vendas")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


}