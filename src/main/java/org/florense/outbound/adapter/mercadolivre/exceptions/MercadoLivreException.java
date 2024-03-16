package org.florense.outbound.adapter.mercadolivre.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MercadoLivreException extends Exception{

    private String message;
    private String method;
    private MLErrorTypesEnum mlErrorTypesEnum;
    private MercadoLivreClientException clientException;
}
