package com.n4systems.model;

import com.n4systems.model.api.Listable;

public enum PriorityCodeAutoScheduleType implements Listable {

    TODAY("label.priority_same_day"), TOMORROW("label.priority_next_day"), END_OF_WEEK("label.priority_end_of_week"), IN_30_DAYS("label.priority_30_days"), CUSTOM("label.priority_custom");

    String label;

    PriorityCodeAutoScheduleType(String label) {
        this.label = label;
    }

    @Override
    public Object getId() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return label;
    }

}
