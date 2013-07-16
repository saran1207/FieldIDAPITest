package com.n4systems.services.search.field;

import com.n4systems.services.search.AnalyzerFactory;

public interface IndexField {

    public AnalyzerFactory.Type getAnalyzerType();

    public String getField();

    int getBoost();

}
