package com.n4systems.model.search;

import com.n4systems.model.api.Listable;
import com.n4systems.util.EnumUtils;

import java.util.EnumSet;

// TODO : rename this to avoid confusion.  WorkflowStateCriteria or something???
public enum WorkflowState implements Listable {

    COMPLETE("label.complete"), OPEN("label.open"), CLOSED("label.closed"), ALL("label.all");

    public static EnumUtils.LabelledEnumSet ALL_STATES = new EnumUtils.LabelledEnumSet<WorkflowState>("All", EnumSet.allOf(WorkflowState.class));

    private String labelKey;

    WorkflowState(String labelKey) {
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

    // For the purposes of reporting, closed events are considered incomplete.
    // (Search parameters apply to the asset rather than the completed event).
    public boolean includesIncomplete() {
        return this == ALL || this == OPEN || this == CLOSED;
    }

    public boolean includesComplete() {
        return this == ALL || this == COMPLETE;
    }

}
