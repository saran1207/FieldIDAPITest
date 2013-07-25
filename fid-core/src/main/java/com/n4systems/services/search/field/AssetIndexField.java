package com.n4systems.services.search.field;

import com.google.common.base.Preconditions;
import com.n4systems.services.search.AnalyzerFactory;

import java.util.EnumSet;

public enum AssetIndexField implements IndexField {

    ID("_id", AnalyzerFactory.Type.KEYWORD),
    SECONDARY_ID("_secondaryorgid", AnalyzerFactory.Type.KEYWORD),
    CUSTOMER_ID("_customerorgid", AnalyzerFactory.Type.KEYWORD),
    DIVISION_ID("_divisionorgid", AnalyzerFactory.Type.KEYWORD),
    CREATED("assetcreated", AnalyzerFactory.Type.KEYWORD, 1),
    MODIFIED("assetmodified", AnalyzerFactory.Type.KEYWORD, 1),
    IDENTIFIER("id", 10),
    TYPE("at",9),
    CUSTOMER("cu",9),
    DIVISION("div",9),
    LOCATION("location",5),
    INTERNAL_ORG("org",9),
    IDENTIFIED("assetidentified", 5),
    RFID("rfid", 9),
    REFERENCE_NUMBER("ref", 9),
    PURCHASE_ORDER("po", 9),
    COMMENTS("comments",AnalyzerFactory.Type.STANDARD,1),
    ORDER("order", 1),
    LAST_EVENT_DATE("lasteventdate", AnalyzerFactory.Type.KEYWORD,1),
    CREATED_BY("usercreated",1),
    MODIFIED_BY("usermodified",1),
    IDENTIFIED_BY("identifiedby",5),
    ASSIGNED("assigned",5),
    OWNER("owner",9),
    TYPE_GROUP("atg",5),
    STATUS("assetstatus",5),
    //note that there is a subtle difference here...the ALL field uses WHITESPACE while some of the fields it contains use the STANDARD analyzer;
    // this may lead to inconsistent results.
    //  i.e. search for "blah-blah-blah"  compared to searching for "comments:blah-blah-blah" will yield different results due to different analyzers
    //  different processing of hyphen.
    ALL("_all", AnalyzerFactory.Type.WHITESPACE);

    private static EnumSet<AssetIndexField> displayedFixedAttributes = EnumSet.of(LOCATION, TYPE, IDENTIFIED, RFID, PURCHASE_ORDER, INTERNAL_ORG, CUSTOMER, DIVISION);
    private static EnumSet<AssetIndexField> nonDisplayedFixedAttributes = EnumSet.of( CREATED, MODIFIED, MODIFIED_BY, REFERENCE_NUMBER, COMMENTS, ORDER, LAST_EVENT_DATE,
            CREATED_BY, MODIFIED_BY, IDENTIFIED_BY, ASSIGNED, TYPE_GROUP, STATUS);
    private static EnumSet<AssetIndexField> internalAttributes = EnumSet.of(ALL, ID, CUSTOMER_ID, DIVISION_ID, SECONDARY_ID);
    private static EnumSet<AssetIndexField> longAttributes= EnumSet.of(ID, CUSTOMER_ID, DIVISION_ID, SECONDARY_ID);

    private final String field;
    private int boost = DEFAULT_BOOST;
    private AnalyzerFactory.Type analyzerType = AnalyzerFactory.Type.WHITESPACE;

    AssetIndexField(String field,AnalyzerFactory.Type analyzerType, int boost) {
        Preconditions.checkArgument(field.equals(field.toLowerCase()), "WARNING : since all fields are index as lowercase, you should not be using uppercase in " + field);
        this.field = field;
        this.boost = boost;
        this.analyzerType = analyzerType;
    }

    AssetIndexField(String field, AnalyzerFactory.Type analyzerType) {
        this(field,analyzerType,DEFAULT_BOOST);
    }

    AssetIndexField(String field, int boost) {
        this(field,AnalyzerFactory.Type.STANDARD,boost);
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

    @Override
    public int getBoost() {
        return boost;
    }

    @Override
    public boolean isLong() {
        return longAttributes.contains(this);
    }

    @Override
    public AnalyzerFactory.Type getAnalyzerType() {
        return analyzerType;
    }

}