package com.n4systems.model.tenant;

import com.n4systems.model.user.Assignable;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SystemSettings implements Serializable {

	public boolean assignedTo;
	public boolean proofTestIntegration;
	public boolean manufacturerCertificate;
	public boolean orderDetails;
	public String dateFormat;
	public String identifierLabel;
	public String identifierFormat;
	public boolean gpsCapture;
	public String supportUrl;
    public String logoutUrl;
    public Assignable procedureApprover;

	public void setSupportUrl(String supportUrl) {
		this.supportUrl = supportUrl;
	}

	public String getSupportUrl() {
		return supportUrl;
	}

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

    public String getIdentifierFormat() {
        return identifierFormat;
    }

    public void setIdentifierFormat(String identifierFormat) {
        this.identifierFormat = identifierFormat;
    }

    public boolean isGpsCapture() {
        return gpsCapture;
    }

    public void setGpsCapture(boolean gpsCapture) {
        this.gpsCapture = gpsCapture;
    }

    public String getIdentifierLabel() {
        return identifierLabel;
    }

    public void setIdentifierLabel(String identifierLabel) {
        this.identifierLabel = identifierLabel;
    }

	public boolean isOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(boolean orderDetails) {
		this.orderDetails = orderDetails;
	}

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public Assignable getProcedureApprover() {
        return procedureApprover;
    }

    public void setProcedureApprover(Assignable procedureApprover) {
        this.procedureApprover = procedureApprover;
    }
}
