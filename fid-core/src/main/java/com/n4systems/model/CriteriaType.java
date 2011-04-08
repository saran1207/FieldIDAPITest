package com.n4systems.model;

public enum CriteriaType {

    ONE_CLICK("One-Click Button", "oneclick"),
    TEXT_FIELD("Text Field", "textfield"),
    COMBO_BOX("Combo Box", "select"),
    SELECT("Select Box", "select"),
    UNIT_OF_MEASURE("Unit of Measure", "unitofmeasure"),
    SIGNATURE("Signature", "signature");

    private String description;
    private String reportIdentifier;

    CriteriaType(String description, String reportIdentifier) {
        this.description = description;
        this.reportIdentifier = reportIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public String getReportIdentifier() {
        return reportIdentifier;
    }

}
