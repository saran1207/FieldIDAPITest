package com.n4systems.fieldid.ws.v1.resources.tenant;

import com.n4systems.model.procedure.LockoutReason;

import java.util.List;

public class ApiTenant {
	private String serialNumberLabel;
	private String serialNumberFormat;
	private String serialNumberDecimalFormat;
	private boolean usingAssignedTo;
	private boolean usingJobSites;
	private boolean usingAdvancedLocation;
	private boolean usingOrderDetails;
	private boolean usingGpsCapture;
    private boolean usingLoto;
	private int maxAttempts;
	private int lockoutDuration;
	private boolean lockoutOnMobile;
	private boolean usingEvents;
	private String applicationProcess;
	private String removalProcess;
	private String testingAndVerification;
	private List<LockoutReason> lockoutReasonList;

	public void setSerialNumberLabel(String serialNumberLabel) {
		this.serialNumberLabel = serialNumberLabel;
	}

	public String getSerialNumberLabel() {
		return serialNumberLabel;
	}

	public String getSerialNumberDecimalFormat() {
		return serialNumberDecimalFormat;
	}

	public void setSerialNumberDecimalFormat(String serialNumberDecimalFormat) {
		this.serialNumberDecimalFormat = serialNumberDecimalFormat;
	}

	public void setUsingAssignedTo(boolean usingAssignedTo) {
		this.usingAssignedTo = usingAssignedTo;
	}

	public boolean isUsingAssignedTo() {
		return usingAssignedTo;
	}

	public void setUsingJobSites(boolean usingJobSites) {
		this.usingJobSites = usingJobSites;
	}

	public boolean isUsingJobSites() {
		return usingJobSites;
	}

	public void setUsingAdvancedLocation(boolean usingAdvancedLocation) {
		this.usingAdvancedLocation = usingAdvancedLocation;
	}

	public boolean isUsingAdvancedLocation() {
		return usingAdvancedLocation;
	}

	public void setUsingOrderDetails(boolean usingOrderDetails) {
		this.usingOrderDetails = usingOrderDetails;
	}

	public boolean isUsingOrderDetails() {
		return usingOrderDetails;
	}

	public void setUsingGpsCapture(boolean usingGpsCapture) {
		this.usingGpsCapture = usingGpsCapture;
	}

	public boolean isUsingGpsCapture() {
		return usingGpsCapture;
	}

	public void setSerialNumberFormat(String serialNumberFormat) {
		this.serialNumberFormat = serialNumberFormat;
	}

	public String getSerialNumberFormat() {
		return serialNumberFormat;
	}

    public boolean isUsingLoto() {
        return usingLoto;
    }

    public void setUsingLoto(boolean usingLoto) {
        this.usingLoto = usingLoto;
    }

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public int getLockoutDuration() {
		return lockoutDuration;
	}

	public void setLockoutDuration(int lockoutDuration) {
		this.lockoutDuration = lockoutDuration;
	}

	public boolean isLockoutOnMobile() {
		return lockoutOnMobile;
	}

	public void setLockoutOnMobile(boolean lockoutOnMobile) {
		this.lockoutOnMobile = lockoutOnMobile;
	}

	public boolean isUsingEvents() {
		return usingEvents;
	}

	public void setUsingEvents(boolean usingEvents) {
		this.usingEvents = usingEvents;
	}

	public String getApplicationProcess() {
		return applicationProcess;
	}

	public void setApplicationProcess(String applicationProcess) {
		this.applicationProcess = applicationProcess;
	}

	public String getRemovalProcess() {
		return removalProcess;
	}

	public void setRemovalProcess(String removalProcess) {
		this.removalProcess = removalProcess;
	}

	public String getTestingAndVerification() {
		return testingAndVerification;
	}

	public void setTestingAndVerification(String testingAndVerification) {
		this.testingAndVerification = testingAndVerification;
	}

	public List<LockoutReason> getLockoutReasonList() {
		return lockoutReasonList;
	}

	public void setLockoutReasonList(List<LockoutReason> lockoutReasonList) {
		this.lockoutReasonList = lockoutReasonList;
	}
}
