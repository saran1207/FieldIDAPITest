package com.n4systems.model.search;

import com.n4systems.model.api.Listable;
import com.n4systems.util.EnumUtils;

import java.util.EnumSet;

public enum EventState implements Listable {

    COMPLETE("label.complete"), OPEN("label.open"), CLOSED("label.closed"), ALL("label.all");

    public static EnumUtils.LabelledEnumSet ALL_STATES = new EnumUtils.LabelledEnumSet<EventState>("All", EnumSet.allOf(EventState.class));

    private String labelKey;

    EventState(String labelKey) {
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

    public boolean includesIncomplete() {
        return this == ALL || this == OPEN;
    }

    public boolean includesComplete() {
        return this == ALL || this == COMPLETE || this == CLOSED;
    }

}
