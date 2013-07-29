package com.n4systems.services.search.field;

import com.google.common.base.Preconditions;
import com.n4systems.services.search.AnalyzerFactory;

import java.util.EnumSet;

public enum AssetIndexField implements IndexField {

    ID("_id", AnalyzerFactory.Type.KEYWORD),
    SECONDARY_ID("_secondaryorgid", AnalyzerFactory.Type.KEYWORD),
    CUSTOMER_ID("_customerorgid", AnalyzerFactory.Type.KEYWORD),
    DIVISION_ID("_divisionorgid", AnalyzerFactory.Type.KEYWORD),
    CREATED("assetcreated", AnalyzerFactory.Type.KEYWORD, LOW_PRIORITY),
    MODIFIED("assetmodified", AnalyzerFactory.Type.KEYWORD, LOW_PRIORITY),
    IDENTIFIER("id", TOP_PRIORITY),
    TYPE("at",HIGH_PRIORITY),
    CUSTOMER("cu",HIGH_PRIORITY),
    DIVISION("div",HIGH_PRIORITY),
    LOCATION("location",MEDIUM_PRIORITY),
    INTERNAL_ORG("org",HIGH_PRIORITY),
    IDENTIFIED("assetidentified", MEDIUM_PRIORITY),
    RFID("rfid", HIGH_PRIORITY),
    REFERENCE_NUMBER("ref", HIGH_PRIORITY),
    PURCHASE_ORDER("po", HIGH_PRIORITY),
    COMMENTS("comments",AnalyzerFactory.Type.STANDARD,LOW_PRIORITY),
    ORDER("order", LOW_PRIORITY),
    LAST_EVENT_DATE("lasteventdate", AnalyzerFactory.Type.KEYWORD,LOW_PRIORITY),
    CREATED_BY("usercreated",LOW_PRIORITY),
    MODIFIED_BY("usermodified",LOW_PRIORITY),
    IDENTIFIED_BY("identifiedby",MEDIUM_PRIORITY),
    ASSIGNED("assigned",MEDIUM_PRIORITY),
    OWNER("owner",HIGH_PRIORITY),
    TYPE_GROUP("atg",MEDIUM_PRIORITY),
    STATUS("assetstatus",MEDIUM_PRIORITY),
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
    private static EnumSet<AssetIndexField> dateAttributes= EnumSet.of(CREATED, MODIFIED, IDENTIFIED, LAST_EVENT_DATE);

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
    public boolean isDate() {
        return dateAttributes.contains(this);
    }

    @Override
    public AnalyzerFactory.Type getAnalyzerType() {
        return analyzerType;
    }

}