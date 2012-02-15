package com.n4systems.model.search;

import com.n4systems.model.api.Listable;

public enum EventStatus implements Listable {

    COMPLETE("label.complete"),INCOMPLETE("label.incomplete"),ALL("label.all");

    private String labelKey;

    EventStatus(String labelKey) {
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
