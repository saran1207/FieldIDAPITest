package com.n4systems.services.reporting;

import java.io.Serializable;
import java.util.Date;

public class WorkSummaryRecord implements Serializable {
    private Date date;
    private Long count;

    public WorkSummaryRecord(Long count, Date date) {
        this.count = count;
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public Date getDate() {
        return date;
    }
}
