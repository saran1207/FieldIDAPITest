package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.preconfigureddevices;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

public class ApiPreconfiguredDevice extends ApiReadonlyModel {

    private String isolationPointSourceType;
    private String device;
    private String method;

    public String getIsolationPointSourceType() {
        return isolationPointSourceType;
    }

    public void setIsolationPointSourceType(String isolationPointSourceType) {
        this.isolationPointSourceType = isolationPointSourceType;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
