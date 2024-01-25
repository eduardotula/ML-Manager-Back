package org.florense.inbound.adapter.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendaDto {

    private Long id;
    private int quantidade;
    private double precoDesconto;
    private double taxaML;
    private double custoFrete;
    private double custo;
    private double lucro;
    private boolean completo;
    private String status;

    private AnuncioVendaDto anuncio;

    private LocalDateTime createdAt;
}
