package com.n4systems.fieldid.service.event.massevent;

import com.n4systems.model.EventType;

import java.io.Serializable;

public class SelectedEventTypeCount implements Serializable {

    public EventType type;
    public Long count;

    public SelectedEventTypeCount(EventType type, Long count) {
        this.type = type;
        this.count = count;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}