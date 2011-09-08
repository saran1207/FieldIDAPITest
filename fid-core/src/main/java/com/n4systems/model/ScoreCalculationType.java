package com.n4systems.model;

public enum ScoreCalculationType {

    SUM("label.calculation.sum"),
    AVERAGE("label.calculation.average");

    ScoreCalculationType(String label) {
        this.label = label;
    }

    String label;

    public String getLabel() {
        return label;
    }

}
