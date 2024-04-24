package org.florense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reclamacao {

    private long id;
    private long reclamacaoId;
    private long resourceId;
    private long orderId;
    private String type;
    private String stage;
    private String status;
    private String reason;
    private String closedBy;
    private String benefited;
    private LocalDateTime createdAt;
    private LocalDateTime reclamacaoCreatedAt;
}
