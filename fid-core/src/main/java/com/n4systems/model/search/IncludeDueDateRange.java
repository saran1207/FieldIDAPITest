package com.n4systems.model.search;

import com.n4systems.model.api.Listable;

public enum IncludeDueDateRange implements Listable {

    HAS_A_DUE_DATE("label.has_a_due_date"), HAS_NO_DUE_DATE("label.has_no_due_date"), SELECT_DUE_DATE_RANGE("label.select_due_date");

    private String labelKey;

    IncludeDueDateRange(String labelKey) {
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
