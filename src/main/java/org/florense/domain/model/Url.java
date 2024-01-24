package org.florense.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.florense.outbound.adapter.postgre.entity.AnuncioEntity;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

public class Url {


    private Long id;

    private String url;

    private long anuncioId;

}
