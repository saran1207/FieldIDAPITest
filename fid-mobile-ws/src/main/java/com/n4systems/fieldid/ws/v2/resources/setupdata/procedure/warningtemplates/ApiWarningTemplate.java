package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.warningtemplates;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

public class ApiWarningTemplate extends ApiReadonlyModel {
    private String name;
    private String warning;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
