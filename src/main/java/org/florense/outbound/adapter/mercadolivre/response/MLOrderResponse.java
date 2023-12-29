package org.florense.outbound.adapter.mercadolivre.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MLOrderResponse {

    @JsonProperty("id")
    private Long orderId;

    @JsonProperty("pack_id")
    private Long packId;

    @JsonProperty("date_closed")
    private LocalDateTime orderCreationTime;
    private String status;
}
