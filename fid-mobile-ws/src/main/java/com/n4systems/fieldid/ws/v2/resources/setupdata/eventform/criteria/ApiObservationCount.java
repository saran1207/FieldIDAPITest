package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform.criteria;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

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
