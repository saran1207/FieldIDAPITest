package com.n4systems.fieldid.ws.v2.resources.setupdata.eventtype.criteria;

import com.google.common.collect.Lists;

import java.util.List;

public class ApiObservationCountCriteria extends ApiCriteria {

    private List<ApiObservationCount> observationCounts;

    public ApiObservationCountCriteria(List<ApiObservationCount> observationCounts) {
        super("OBSERVATION_COUNT");
        this.observationCounts = observationCounts;
    }

    public ApiObservationCountCriteria() {
        this(Lists.newArrayList());
    }

    public List<ApiObservationCount> getObservationCounts() {
        return observationCounts;
    }

    public void setObservationCounts(List<ApiObservationCount> observationCounts) {
        this.observationCounts = observationCounts;
    }
}
