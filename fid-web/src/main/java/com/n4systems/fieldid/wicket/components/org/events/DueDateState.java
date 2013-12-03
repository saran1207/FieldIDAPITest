package com.n4systems.fieldid.wicket.components.org.events;


import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.api.DisplayEnum;

public enum DueDateState implements DisplayEnum
{
    UPCOMING(new FIDLabelModel("label.upcoming").getObject()),
    OVERDUE(new FIDLabelModel("label.overdue").getObject());

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
