package com.n4systems.model.search;

import com.n4systems.model.api.Listable;
import com.n4systems.util.EnumUtils;

import java.util.EnumSet;

public enum ProcedureWorkflowStateCriteria implements Listable {

    OPEN("label.open"), LOCKED("label.locked"), UNLOCKED("label.unlocked"), ALL("label.all");

    public static EnumUtils.LabelledEnumSet ALL_STATES = new EnumUtils.LabelledEnumSet<WorkflowStateCriteria>("All", EnumSet.allOf(WorkflowStateCriteria.class));

    private String labelKey;

    ProcedureWorkflowStateCriteria(String labelKey) {
        this.labelKey = labelKey;
    }

    @Override
    public Object getId() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return labelKey;
    }

}
