package com.n4systems.fieldid.selenium.testcase.setup.eventform;

public enum CriteriaType {

    ONE_CLICK("One-Click Button"), TEXT_FIELD("Text Field"), COMBO_BOX("Combo Box"), SELECT("Select Box"), UNIT_OF_MEASURE("Unit of Measure");

    private String description;

    CriteriaType(String description) {
        this.description = description;
    }

}
