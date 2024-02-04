package org.florense.domain.model.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendaFilter {
    private LocalDateTime orderCreationInicial = null;
    private LocalDateTime orderCreationFinal = null;
    private boolean includeCancelled = false;
}
