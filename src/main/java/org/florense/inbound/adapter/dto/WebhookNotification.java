package org.florense.inbound.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookNotification {

    private String resource;
    @JsonProperty("user_id")
    private String userIdML;
    private String topic;
    @JsonProperty("application_id")
    private String applicationId;
    private int attempts;
    private LocalDateTime sent;
    private LocalDateTime received;

}
