package org.florense.domain.model;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Venda {

    private Long id;
    private int quantidade;
    private double precoDesconto;
    private double taxaML;
    private double custoFrete;
    private double custo;
    private double lucro;
    private Long orderId;
    private Long anuncioId;
    private LocalDateTime createdAt;
}
