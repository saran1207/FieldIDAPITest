package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.energysources;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel;

public class ApiEnergySource extends ApiReadOnlyModel {
    private String isolationPointSourceType;
    private String source;

    public String getIsolationPointSourceType() {
        return isolationPointSourceType;
    }

    public void setIsolationPointSourceType(String isolationPointSourceType) {
        this.isolationPointSourceType = isolationPointSourceType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
