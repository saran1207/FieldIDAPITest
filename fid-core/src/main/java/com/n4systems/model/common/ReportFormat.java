package com.n4systems.model.common;

import com.n4systems.model.api.Listable;

public enum ReportFormat implements Listable<String> {

    HTML("label.html"),EXCEL("label.excel");
    
    private String label;
    
    ReportFormat(String label) {
        this.label = label;
    }

    @Override
    public String getId() {
        return label;
    }

    @Override
    public String getDisplayName() {
        return label;
    }
}
