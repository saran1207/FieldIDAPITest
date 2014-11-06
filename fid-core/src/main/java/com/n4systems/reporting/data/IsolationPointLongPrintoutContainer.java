package com.n4systems.reporting.data;

import java.io.File;

/**
 * This POJO is used to hold information on Isolation Points for use in the new LOTO Short and Long Printouts.
 *
 * Created by jheath on 2014-10-30.
 */
public class IsolationPointLongPrintoutContainer {

    private String energySource;
    private String device;
    private String lockoutMethod;

    //We use this so that the image itself can be fed directly to the report.  These objects get initialised with a
    //byte array filled with the image data.
    private File image;

    public String getEnergySource() {
        return energySource;
    }

    public void setEnergySource(String energySource) {
        this.energySource = energySource;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLockoutMethod() {
        return lockoutMethod;
    }

    public void setLockoutMethod(String lockoutMethod) {
        this.lockoutMethod = lockoutMethod;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
