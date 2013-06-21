package com.n4systems.services.search;

import com.google.common.base.Preconditions;

import java.util.EnumSet;

public enum AssetIndexField {
    ID("_id"),
    SECONDARY_ID("_secondaryorgid"),
    CUSTOMER_ID("_customerorgid"),
    DIVISION_ID("_divisionorgid"),
    CREATED("assetcreated",1),
    MODIFIED("assetmodified",1),
    IDENTIFIER("id",10),
    RFID("rfid",9),
    REFERENCE_NUMBER("ref",9),
    PURCHASE_ORDER("po",9),
    COMMENTS("comments",1),
    IDENTIFIED("assetidentified",5),
    ORDER("order",1),
    LAST_EVENT_DATE("lasteventdate",1),
    LOCATION("location",5),
    CREATED_BY("usercreated",1),
    MODIFIED_BY("usermodified",1),
    IDENTIFIED_BY("identifiedby",5),
    ASSIGNED("assigned",5),
    OWNER("owner",9),
    INTERNAL_ORG("org",9),
    CUSTOMER("cu",9),
    DIVISION("div",9),
    TYPE("at",9),
    TYPE_GROUP("atg",5),
    STATUS("assetstatus",5),
    ALL("_all");

    private static EnumSet<AssetIndexField> displayedFixedAttributes = EnumSet.of(LOCATION, TYPE, IDENTIFIED, RFID, PURCHASE_ORDER, INTERNAL_ORG, CUSTOMER, DIVISION);
    private static EnumSet<AssetIndexField> nonDisplayedFixedAttributes = EnumSet.of( CREATED, MODIFIED, MODIFIED_BY, REFERENCE_NUMBER, COMMENTS, ORDER, LAST_EVENT_DATE,
            CREATED_BY, MODIFIED_BY, IDENTIFIED_BY, ASSIGNED, TYPE_GROUP, STATUS);
    private static EnumSet<AssetIndexField> internalAttributes = EnumSet.of(ALL, ID, CUSTOMER_ID, DIVISION_ID, SECONDARY_ID);

    private final String field;
    private int boost = 1;

    AssetIndexField(String field, int boost) {
        Preconditions.checkArgument(field.equals(field.toLowerCase()), "WARNING : since all fields are index as lowercase, you should not be using uppercase in " + field);
        this.field = field;
        this.boost = boost;
    }

    AssetIndexField(String field) {
        this(field,1);
    }

    public String getField() {
        return field;
    }

    public static AssetIndexField fromString(String s) {
        if (s!=null) {
            s = s.toLowerCase().trim();
            for (AssetIndexField field:values()) {
                if (field.getField().equalsIgnoreCase(s)) {
                    return field;
                }
            }
        }
        return null;
    }

    public static EnumSet<AssetIndexField> getDisplayedFixedAttributes() {
        return displayedFixedAttributes;
    }

    public static EnumSet<AssetIndexField> getNonDisplayedFixedAttributes() {
        return nonDisplayedFixedAttributes;
    }

    public boolean isInternal() {
        return internalAttributes.contains(this);
    }

    public boolean isNonDisplayedFixedAttribute() {
        return nonDisplayedFixedAttributes.contains(this);
    }

    public boolean isDisplayedFixedAttribute() {
        return displayedFixedAttributes.contains(this);
    }

    public int getBoost() {
        return boost;
    }
}