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
public class OrderFilter {
    private LocalDateTime orderCreationInicial = null;
    private LocalDateTime orderCreationFinal = null;

}
