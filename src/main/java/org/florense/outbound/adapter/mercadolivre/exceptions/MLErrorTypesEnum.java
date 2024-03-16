package org.florense.outbound.adapter.mercadolivre.exceptions;

public enum MLErrorTypesEnum {

    FRETE_VALUE("erro ao tentar obter frete de um produto que está no full e desativado sem estoque", "frete"),
    DEFAULT("Erro generico", "default");
    private final String description;
    private final String identifier;

    MLErrorTypesEnum(String description, String identifier) {
        this.description = description;
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentifier() {
        return identifier;
    }
}
