package com.n4systems.model;

import com.n4systems.model.api.DisplayEnum;

public enum ProcedureWorkflowState implements DisplayEnum {

    OPEN("Open"), LOCKED("Locked"), UNLOCKED("Unlocked"), CLOSED("Closed");

    private String label;

    ProcedureWorkflowState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

}
