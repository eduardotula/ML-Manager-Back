package org.florense.inbound.adapter.dto.anuncios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnuncioDtoSimple {

    @NotBlank
    private String mlId;
    @NotNull
    private double custo;
    @NotNull
    private String csosn;
}
