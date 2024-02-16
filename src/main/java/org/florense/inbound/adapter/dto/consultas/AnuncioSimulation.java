package org.florense.inbound.adapter.dto.consultas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.florense.domain.model.ListingTypeEnum;

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
    private String csosn;
    private ListingTypeEnum tipoAnuncio;
    private Long userId;

}
