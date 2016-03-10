package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

/**
 * Base class for all Criteria Rule data ported through the API.
 *
 * Created by Jordan Heath on 2016-03-10.
 */
public class ApiCriteriaRule extends ApiReadonlyModel {
    private long criteriaId;
    private String action;

    public long getCriteriaId() {
        return criteriaId;
    }

    public ApiCriteriaRule setCriteriaId(long criteriaId) {
        this.criteriaId = criteriaId;
        return this;
    }

    public String getAction() {
        return action;
    }

    public ApiCriteriaRule setAction(String action) {
        this.action = action;
        return this;
    }
}
