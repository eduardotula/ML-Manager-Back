package org.florense.domain.model.enums;

public enum NotificationTopicEnum {

    ITEMS("items"),
    ORDERS("orders_v2"),
    CLAIMS("claims");

    private final String value;

    NotificationTopicEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
