package com.n4systems.fieldid.wicket.components.org.events;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.api.DisplayEnum;

public enum ScheduledState implements DisplayEnum
{
    SCHEDULED(new FIDLabelModel("label.upcoming").getObject()),
    UNSCHEDULED(new FIDLabelModel("label.overdue").getObject());

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
