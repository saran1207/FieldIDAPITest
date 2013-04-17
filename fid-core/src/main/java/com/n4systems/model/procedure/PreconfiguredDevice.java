package com.n4systems.model.procedure;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "preconfigured_devices")
public class PreconfiguredDevice extends BaseEntity implements UnsecuredEntity {

    @Enumerated(EnumType.STRING)
    private IsolationPointSourceType isolationPointSourceType;

    private String device;

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
}
