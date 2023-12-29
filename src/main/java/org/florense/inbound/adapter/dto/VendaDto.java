package org.florense.inbound.adapter.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendaDto {

    private Long id;
    private double precoDesconto;
    private double taxaML;
    private double custoFrete;
    private double custo;
    private double lucro;
    private String status;
    private boolean completo;
    private LocalDateTime createdAt;
}