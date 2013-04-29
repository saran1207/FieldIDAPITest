package com.n4systems.model;

import com.n4systems.model.api.DisplayEnum;

public enum ProcedureWorkflowState implements DisplayEnum {

    OPEN("Open"), LOCKED("Locked"), UNLOCKED("Unlocked"), CLOSED("Closed");
    public static ProcedureWorkflowState[] ACTIVE_STATES = { OPEN, LOCKED };

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
