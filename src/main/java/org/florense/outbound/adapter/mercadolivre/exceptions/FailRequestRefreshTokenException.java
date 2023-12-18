package org.florense.outbound.adapter.mercadolivre.exceptions;

public class FailRequestRefreshTokenException extends Exception{

    private String message;
    private Exception exception;

    public FailRequestRefreshTokenException(String message){
        this.message = message;
        this.exception = new Exception(message);
    }
}
