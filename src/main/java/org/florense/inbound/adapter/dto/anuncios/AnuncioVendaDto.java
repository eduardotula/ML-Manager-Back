package org.florense.inbound.adapter.dto.anuncios;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnuncioVendaDto {

    private Long id;
    private String mlId;
    private String descricao;
    private boolean complete;
    private String fotoCapa;
}
