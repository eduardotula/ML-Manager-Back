package org.florense.inbound.adapter.dto.consultas;

import lombok.*;
import org.florense.domain.model.enums.ListingTypeEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnuncioSimulation {

    private String categoria;
    private double valorVenda;
    private double custo;
    private double custoFrete;
    private String equivalentMlId;
    private String csosn;
    private ListingTypeEnum tipoAnuncio;
    private Long userId;

}
