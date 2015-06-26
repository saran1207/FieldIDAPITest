package com.n4systems.fieldid.api.mobile.resources.eventtype.criteria;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadonlyModel;

public class ApiObservationCount extends ApiReadonlyModel {

    private String name;
    private Boolean isCounted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isCounted() {
        return isCounted;
    }

    public void setCounted(Boolean counted) {
        isCounted = counted;
    }
}
