package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

/**
 * Select Criteria Rule...
 *
 * Created by Jordan Heath on 2016-03-10.
 */
public class ApiSelectCriteriaRule extends ApiCriteriaRule {
    private String selectValue;

    public String getSelectValue() {
        return selectValue;
    }

    public ApiSelectCriteriaRule setSelectValue(String selectValue) {
        this.selectValue = selectValue;
        return this;
    }
}
