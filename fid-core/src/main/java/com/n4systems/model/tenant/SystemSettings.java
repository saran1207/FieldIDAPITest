package com.n4systems.model.tenant;

import java.io.Serializable;

public class SystemSettings implements Serializable {

    public boolean assignedTo;
    public boolean proofTestIntegration;
    public boolean manufacturerCertificate;
    public String dateFormat;
    public String serialNumberFormat;
    public boolean gpsCapture;

    public boolean isAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(boolean assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isProofTestIntegration() {
        return proofTestIntegration;
    }

    public void setProofTestIntegration(boolean proofTestIntegration) {
        this.proofTestIntegration = proofTestIntegration;
    }

    public boolean isManufacturerCertificate() {
        return manufacturerCertificate;
    }

    public void setManufacturerCertificate(boolean manufacturerCertificate) {
        this.manufacturerCertificate = manufacturerCertificate;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getSerialNumberFormat() {
        return serialNumberFormat;
    }

    public void setSerialNumberFormat(String serialNumberFormat) {
        this.serialNumberFormat = serialNumberFormat;
    }

    public boolean isGpsCapture() {
        return gpsCapture;
    }

    public void setGpsCapture(boolean gpsCapture) {
        this.gpsCapture = gpsCapture;
    }
}
