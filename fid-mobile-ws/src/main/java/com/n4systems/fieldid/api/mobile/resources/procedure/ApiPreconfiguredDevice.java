package com.n4systems.fieldid.api.mobile.resources.procedure;

/**
 * Created by rrana on 2015-01-07.
 */
public class ApiPreconfiguredDevice {

    private String isolationPointSourceType;
    private String device;
    private String method;

    public ApiPreconfiguredDevice() {
        //Do nothing, look pretty...
    }

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
