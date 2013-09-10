package com.n4systems.services.search.field;

import com.n4systems.services.search.AnalyzerFactory;

public interface IndexField {

    static final int DEFAULT_BOOST = 1;
    static final int TOP_PRIORITY = 10;
    static final int HIGH_PRIORITY = 9;
    static final int LOW_PRIORITY = 1;
    static final int MEDIUM_PRIORITY = 5;


    public AnalyzerFactory.Type getAnalyzerType();

    public String getField();

    int getBoost();

    boolean isLong();

    boolean isDate();

    boolean isInternal();

    boolean isNonDisplayedFixedAttribute();

}
