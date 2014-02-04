package com.n4systems.fieldid.wicket.components.org.events;


import com.n4systems.model.api.DisplayEnum;

public enum DueDateState implements DisplayEnum
{
    UPCOMING("label.upcoming"),
    OVERDUE("label.overdue");

    private String label;

    DueDateState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }
}
