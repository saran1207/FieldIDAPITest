package com.n4systems.services.safetyNetwork.catalog.summary;


public abstract class BaseImportSummary {

	public enum FailureType {
		COULD_NOT_CREATE(), COULD_NOT_CREATE_AUTOATTRIBUTE(), COULD_NOT_CONNECT_SUB_ASSET(), NOT_PUBLISHED(),
		COULD_NOT_LINK_ASSET_TYPE_TO_INSPECTION_TYPE(), COULD_NOT_CREATE_SCHEDULE();
	}
	
	private String failedImporting;
	private Exception failureReason;
	private FailureType failureType;

	public BaseImportSummary() {
	}

	public Exception getFailureReason() {
		return failureReason;
	}

	public void setFailure(String failedImporting, FailureType failureType, Exception failureReason) {
		this.failedImporting = failedImporting;
		this.failureReason = failureReason;
		this.failureType = failureType;
	}

	public boolean failed() {
		return failedImporting != null;
	}

	public String getFailedImporting() {
		return failedImporting;
	}

	public FailureType getFailureType() {
		return failureType;
	}

}