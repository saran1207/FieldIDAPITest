package com.n4systems.reporting.data;

/**
 * This is a simple POJO for holding data used for the Isolation Point printout on the LOTO Short Form.
 *
 * Created by Jordan Heath on 14-11-05.
 */
public class IsolationPointShortPrintoutContainer {

    private String energySource;
    private String sourceId;
    private String device;
    private String sourceLocation;
    private String lockoutMethod;
    private String check;

    public String getEnergySource() {
        return energySource;
    }

    public void setEnergySource(String energySource) {
        this.energySource = energySource;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getLockoutMethod() {
        return lockoutMethod;
    }

    public void setLockoutMethod(String lockoutMethod) {
        this.lockoutMethod = lockoutMethod;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
