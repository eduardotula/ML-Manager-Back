package org.florense.outbound.adapter.mercadolivre.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnauthorizedAcessKeyException extends Exception{

    private String message;
    private Exception exception;

    public UnauthorizedAcessKeyException(String message){
        this.message = message;
        this.exception = new Exception(message);
    }


}
