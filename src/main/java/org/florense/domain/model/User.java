package org.florense.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String name;
    private String userIdML;
    private String accessCode;
    private String refreshToken;
    private LocalDateTime createdAt;
    private String cep;
}
