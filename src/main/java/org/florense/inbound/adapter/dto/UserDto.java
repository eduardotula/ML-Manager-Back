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
public class UserDto {

    private Long id;
    @NotEmpty(message = "name não pode estar vazio")
    private String name;
    @NotEmpty(message = "userIdML não pode estar vazio")
    private String userIdML;
    @NotEmpty(message = "accessCode não pode estar vazio")
    private String accessCode;
    @NotEmpty(message = "refreshToken não pode estar vazio")
    private String refreshToken;
    private LocalDateTime createdAt;
    @NotEmpty(message = "cep não pode estar vazio")
    private String cep;
}
