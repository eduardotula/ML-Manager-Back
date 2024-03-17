package org.florense.outbound.adapter.mercadolivre.exceptions;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MercadoLivreClientException extends Exception{

    private String message;
    private int status;
    private String error;
    @JsonIgnore
    private String detail = "";
    @JsonIgnore
    private String completeError;
    @JsonIgnore
    private boolean refreshToken = false;

    @JsonCreator()
    public MercadoLivreClientException(
            @JsonProperty("message") String message, @JsonProperty("status") int status, @JsonProperty("error") String error, @JsonProperty("cause") List<Map<String, Object>> cause) {
        super(message);
        this.message = message;
        this.status = status;
        this.error = error;
        setDetail(cause);
        this.completeError = "";
        this.refreshToken = false;
    }


    @JsonSetter("cause")
    private void setDetail(List<Map<String, Object>> list){
        if(!list.isEmpty()){
            Object detailO = list.get(1).get("detail");
            this.detail = detailO != null ? (String)detailO : "" ;
        }
    }
}
