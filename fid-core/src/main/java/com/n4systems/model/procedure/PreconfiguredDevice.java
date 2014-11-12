package com.n4systems.model.procedure;

import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

@Entity
@Table(name = "preconfigured_devices")
public class PreconfiguredDevice extends EntityWithTenant {

    @Enumerated(EnumType.STRING)
    @Column(name="source")
    private IsolationPointSourceType isolationPointSourceType;

    private String device;

    private String method;

    public IsolationPointSourceType getIsolationPointSourceType() {
        return isolationPointSourceType;
    }

    public void setIsolationPointSourceType(IsolationPointSourceType isolationPointSourceType) {
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
