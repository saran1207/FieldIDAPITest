package com.n4systems.model.search;

import com.n4systems.model.api.Listable;

public enum EventSearchType implements Listable<String> {

    EVENTS("label.events"),ACTIONS("label.actions");

    private String labelKey;

    EventSearchType(String labelKey) {
        this.labelKey = labelKey;
    }

    @Override
    public String getId() {
        return labelKey;
    }

    @Override
    public String getDisplayName() {
        return labelKey;
    }

}
