package com.n4systems.services.search.field;

import com.n4systems.services.search.AnalyzerFactory;

public interface IndexField {

    static final int DEFAULT_BOOST = 1;

    public AnalyzerFactory.Type getAnalyzerType();

    public String getField();

    int getBoost();

    boolean isLong();

}
