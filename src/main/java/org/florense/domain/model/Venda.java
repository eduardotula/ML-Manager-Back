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
    private double precoDesconto;
    private double taxaML;
    private double custoFrete;
    private double custo;
    private double lucro;
    private String status;
    private boolean completo;
    private Long orderId;
    private Long anuncioId;
    private LocalDateTime createdAt;
}
