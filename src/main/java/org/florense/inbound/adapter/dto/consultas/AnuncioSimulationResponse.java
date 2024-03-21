package org.florense.inbound.adapter.dto.consultas;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnuncioSimulationResponse {

    private double lucro;
    private double custo;
    private String csosn;
    private double taxaMl;
    private double frete;
    private double imposto;
    private String categoria;
}
