package com.n4systems.reporting;

import java.io.Serializable;

/**
 * Created by rrana on 2014-12-09.
 */
public class PriorityCodeListView implements Serializable {
    private static final long serialVersionUID = 1L;

    public PriorityCodeListView(){}

    private String priorityCode;
    private String count;

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
