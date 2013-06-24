package com.n4systems.services.search;

public interface HasAnalyzerType {

    public AnalyzerFactory.Type getAnalyzerType();

    public String getField();
}
