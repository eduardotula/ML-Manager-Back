package org.florense.outbound.adapter.mercadolivre.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MercadoLivreProduto {

    private String mlId;
    private String title;
    private String permalink;
    private String category_id;
    private Float price;
    private String gtin;
    private String sku;

}
