package com.n4systems.model.search;

import com.n4systems.model.api.Listable;

public enum EventState implements Listable {

    COMPLETE("label.complete"),INCOMPLETE("label.incomplete"),ALL("label.all");

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

}
