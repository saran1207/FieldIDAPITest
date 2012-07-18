package com.n4systems.model.search;

import com.n4systems.model.api.Listable;

public enum EventState implements Listable {

    COMPLETE("label.complete"), OPEN("label.open"), CLOSED("label.closed"), ALL("label.all");

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
        return this == ALL || this == COMPLETE;
    }

}
