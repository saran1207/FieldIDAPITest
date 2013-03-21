package com.n4systems.model;

import com.n4systems.model.api.DisplayEnum;

public enum WorkflowState implements DisplayEnum {
    OPEN("Open"), COMPLETED("Completed"), CLOSED("Closed");

    private String label;

    WorkflowState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }
}
