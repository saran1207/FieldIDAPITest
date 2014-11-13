package com.n4systems.reporting.data;

import java.io.InputStream;

/**
 * This is a simple POJO for holding data used for the Isolation Point printout on the LOTO Short Form.
 *
 * Created by Jordan Heath on 14-11-05.
 */
public class IsolationPointPrintoutContainer {

    private String energySource;
    private String sourceId;
    private String device;
    private String sourceLocation;
    private String lockoutMethod;
    private String check;
    private String energyType;

    //We use this so that the image itself can be fed directly to the report.  These objects get initialised with a
    //byte array filled with the image data.
    private InputStream image;

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

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }
}
