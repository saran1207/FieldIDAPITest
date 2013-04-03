package com.n4systems.model.procedure;

import com.n4systems.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "isolation_points")
public class IsolationPoint extends BaseEntity {

    @Column(name="identifier")
    private String identifier;

    @Column(name="source")
    private String source;

    @OneToOne
    @JoinColumn(name="device_definition_id")
    private IsolationDeviceDescription deviceDefinition;

    @OneToOne
    @JoinColumn(name="lock_definition_id")
    private IsolationDeviceDescription lockDefinition;

    @Column(name="location")
    private String location;

    @Column(name="method")
    private String method;

    @Column(name="check_str")
    private String check;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public IsolationDeviceDescription getDeviceDefinition() {
        return deviceDefinition;
    }

    public void setDeviceDefinition(IsolationDeviceDescription deviceDefinition) {
        this.deviceDefinition = deviceDefinition;
    }

    public IsolationDeviceDescription getLockDefinition() {
        return lockDefinition;
    }

    public void setLockDefinition(IsolationDeviceDescription lockDefinition) {
        this.lockDefinition = lockDefinition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

}
