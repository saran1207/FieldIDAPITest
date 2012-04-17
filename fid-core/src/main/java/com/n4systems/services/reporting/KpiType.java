package com.n4systems.services.reporting;

public enum KpiType {
    FAILED("failed"), INCOMPLETE("incomplete"), COMPLETED("completed");

    private String label;

    KpiType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
