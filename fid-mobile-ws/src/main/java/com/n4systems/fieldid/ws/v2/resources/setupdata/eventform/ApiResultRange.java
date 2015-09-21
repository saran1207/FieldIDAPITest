package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform;

/**
 * Created by jheath on 2015-09-16.
 */
public class ApiResultRange {
    private String comparator;
    private Double value1;
    private Double value2;

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }
}
