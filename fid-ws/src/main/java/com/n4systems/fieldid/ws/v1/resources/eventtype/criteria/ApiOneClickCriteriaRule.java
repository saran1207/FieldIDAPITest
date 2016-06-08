package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

/**
 * Bananabananabanana
 *
 * Created by Jordan Heath on 2016-03-10.
 */
public class ApiOneClickCriteriaRule extends ApiCriteriaRule {
    private long oneClickStateId;

    public long getOneClickStateId() {
        return oneClickStateId;
    }

    public ApiOneClickCriteriaRule setOneClickStateId(long oneClickStateId) {
        this.oneClickStateId = oneClickStateId;
        return this;
    }
}
