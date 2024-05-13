package org.florense.inbound.adapter.dto.anuncios;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "102|500",message = "campo tipo do anuncio, valores: 102, 500", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String csosn;
}
