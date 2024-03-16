package org.florense.outbound.adapter.mercadolivre.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @JsonSetter("cause")
    private void setDetail(List<Map<String, Object>> list){
        if(!list.isEmpty()){
            Object detailO = list.get(1).get("detail");
            this.detail = detailO != null ? (String)detailO : "" ;
        }
    }
}
