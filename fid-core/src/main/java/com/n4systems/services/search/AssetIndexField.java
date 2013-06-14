package com.n4systems.services.search;

public enum AssetIndexField {
    ID("_id"),
    SECONDARY_ID("_secondaryOrgId"),
    CUSTOMER_ID("_customerOrgId"),
    DIVISION_ID("_divisionOrgId"),
    CREATED("assetcreated"),
    MODIFIED("assetmodified"),
    IDENTIFIER("id"),
    RFID("rfid"),
    REFERENCE_NUMBER("ref"),
    PURCHASE_ORDER("po"),
    COMMENTS("comments"),
    IDENTIFIED("assetidentifed"),
    ORDER("order"),
    LAST_EVENT_DATE("lasteventdate"),
    LOCATION("location"),
    CREATED_BY("usercreated"),
    MODIFIED_BY("usermodified"),
    IDENTIFIED_BY("identifiedby"),
    ASSIGNED("assigned"),
    OWNER("owner"),
    INTERNAL_ORG("org"),
    CUSTOMER("cu"),
    DIVISION("div"),
    TYPE("at"),
    TYPE_GROUP("atg"),
    STATUS("assetstatus"),
    ALL("_all");

    private final String field;

    AssetIndexField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static AssetIndexField fromString(String s) {
        if (s!=null) {
            for (AssetIndexField field:values()) {
                if (field.getField().equals(s)) {
                    return field;
                }
            }
        }
        return null;
    }
}