package org.florense.domain.model;

public enum ListingTypeEnum {

    classico("gold_special"),
    premium("gold_pro");

    private final String value;

    ListingTypeEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
