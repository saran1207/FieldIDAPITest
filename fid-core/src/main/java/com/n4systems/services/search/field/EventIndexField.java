package com.n4systems.services.search.field;

import com.google.common.base.Preconditions;
import com.n4systems.services.search.AnalyzerFactory;

public enum EventIndexField implements IndexField {
    ID("_id", AnalyzerFactory.Type.KEYWORD),
    SECONDARY_ID("_secondaryorgid", AnalyzerFactory.Type.KEYWORD),
    CUSTOMER_ID("_customerorgid", AnalyzerFactory.Type.KEYWORD),
    DIVISION_ID("_divisionorgid", AnalyzerFactory.Type.KEYWORD),
    CREATED("assetcreated", AnalyzerFactory.Type.KEYWORD, 1),
    MODIFIED("assetmodified", AnalyzerFactory.Type.KEYWORD, 1),
    IDENTIFIER("id", 10),
    EVENT_TYPE("et",9),
    EVENT_TYPE_GROUP("etg",5),
    RESULT("result",9),
    ASSET_TYPE("at",9),
    CUSTOMER("cu",9),
    DIVISION("div",9),
    LOCATION("location",5),
    INTERNAL_ORG("org",9),
    RFID("rfid", 9),
    REFERENCE_NUMBER("ref", 9),
    PURCHASE_ORDER("po", 9),
    COMMENTS("comments",AnalyzerFactory.Type.STANDARD,1),
    NOTES("notes",AnalyzerFactory.Type.STANDARD,1),
    ORDER("order", 1),
    CREATED_BY("usercreated",1),
    MODIFIED_BY("usermodified",1),
    PERFORMED_BY("performedby",5),
    ASSIGNEE("assignee",1),
    OWNER("owner",9),
    ASSET_TYPE_GROUP("atg",5),
    ASSET_STATUS("assetstatus",5),
    WORKFLOW_STATE("state", 5),
    DUE_DATE("due", AnalyzerFactory.Type.KEYWORD, 1),
    COMPLETED_DATE("completed", AnalyzerFactory.Type.KEYWORD, 1),
    SCORE("score", 5),
    ALL("_all", AnalyzerFactory.Type.WHITESPACE);;

    private static final int DEFAULT_BOOST = 1;

    private final String field;
    private int boost = DEFAULT_BOOST;
    private AnalyzerFactory.Type analyzerType = AnalyzerFactory.Type.WHITESPACE;

    EventIndexField(String field,AnalyzerFactory.Type analyzerType, int boost) {
        Preconditions.checkArgument(field.equals(field.toLowerCase()), "WARNING : since all fields are index as lowercase, you should not be using uppercase in " + field);
        this.field = field;
        this.boost = boost;
        this.analyzerType = analyzerType;
    }

    EventIndexField(String field, AnalyzerFactory.Type analyzerType) {
        this(field,analyzerType,DEFAULT_BOOST);
    }

    EventIndexField(String field, int boost) {
        this(field,AnalyzerFactory.Type.STANDARD,boost);
    }

    @Override
    public AnalyzerFactory.Type getAnalyzerType() {
        return analyzerType;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public int getBoost() {
        return boost;
    }

    public static EventIndexField fromString(String s) {
        if (s!=null) {
            s = s.toLowerCase().trim();
            for (EventIndexField field:values()) {
                if (field.getField().equalsIgnoreCase(s)) {
                    return field;
                }
            }
        }
        return null;
    }

}
