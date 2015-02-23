package com.n4systems.fieldid.ws.v1.resources.event.criteria;

import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiObservationCount;

/**
 * This POJO contains the Result portion of an ObservationCountCriteria.
 *
 * This is intentionally NOT using the ApiReadWriteModel or ApiReadOnlyModel ancestry to avoid altering the existing
 * data model.  There is arguably little use for a mobile GUID at this level.  We can do most identification using the
 * contained ApiObservationCount data.
 *
 * Created by Jordan Heath on 15-02-13.
 */
public class ApiObservationCountResult {
    private ApiObservationCount observationCount;
    private Integer value;

    public ApiObservationCount getObservationCount() {
        return observationCount;
    }

    public void setObservationCount(ApiObservationCount observationCount) {
        this.observationCount = observationCount;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
