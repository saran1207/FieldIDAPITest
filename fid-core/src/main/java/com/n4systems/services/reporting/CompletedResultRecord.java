package com.n4systems.services.reporting;

import com.n4systems.model.EventResult;

import java.io.Serializable;

public class CompletedResultRecord implements Serializable {
    public EventResult state;
    public Long count;

    public CompletedResultRecord(EventResult state, Long count) {
        this.state = state;
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public EventResult getState() {
        return state;
    }

    public void setState(EventResult state) {
        this.state = state;
    }
}
