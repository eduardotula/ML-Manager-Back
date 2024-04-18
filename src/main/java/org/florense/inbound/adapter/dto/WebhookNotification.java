package org.florense.inbound.adapter.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookNotification {

    private String resource;
    private String userIdML;
    private String topic;
    private String applicationId;
    private int attempts;
    private LocalDateTime sent;
    private LocalDateTime received;

}
