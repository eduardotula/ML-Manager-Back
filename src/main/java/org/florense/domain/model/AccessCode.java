package org.florense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessCode {

    private Long id;
    private String accessCode;
    private String refreshToken;
    private LocalDateTime createdAt;
}
