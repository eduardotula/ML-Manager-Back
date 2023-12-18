package org.florense.inbound.adapter.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessCodeDto {

    private Long id;
    @NotEmpty
    private String accessCode;
    @NotEmpty
    private String refreshToken;
    private LocalDateTime createdAt;
}
