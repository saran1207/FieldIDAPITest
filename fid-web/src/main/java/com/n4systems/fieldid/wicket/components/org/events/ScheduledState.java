package com.n4systems.fieldid.wicket.components.org.events;

import com.n4systems.model.api.DisplayEnum;

public enum ScheduledState implements DisplayEnum {
    SCHEDULED("label.upcoming"),
    UNSCHEDULED("label.overdue");

    private String label;

    ScheduledState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }
}
