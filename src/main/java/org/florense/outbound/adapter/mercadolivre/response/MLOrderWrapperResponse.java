package org.florense.outbound.adapter.mercadolivre.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MLOrderWrapperResponse {

    @JsonProperty("results")
    List<MLOrderResponse> orderResponses = new ArrayList<>();
}
