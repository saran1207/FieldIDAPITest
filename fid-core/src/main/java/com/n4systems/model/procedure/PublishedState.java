package com.n4systems.model.procedure;

import com.n4systems.model.api.DisplayEnum;

public enum PublishedState implements DisplayEnum {

    DRAFT("Draft"),
    WAITING_FOR_APPROVAL("Waiting for Approval"),
    PUBLISHED("Published"),
    PREVIOUSLY_PUBLISHED("Previously Published");

    public static PublishedState [] ACTIVE_STATES = { DRAFT, WAITING_FOR_APPROVAL, PUBLISHED };

    private String label;

    PublishedState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

    public boolean isPreApproval() {
        return this == DRAFT || this == WAITING_FOR_APPROVAL;
    }

}
