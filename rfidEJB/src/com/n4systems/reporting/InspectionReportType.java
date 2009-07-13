package com.n4systems.reporting;

public enum InspectionReportType { 
	INSPECTION_CERT("inspection_certificate", "Inspection Certificate"), OBSERVATION_CERT("observations_certificate", "Observation Certificate");
	
	private String reportNamePrefix;
	private String displayName;
	
	private InspectionReportType(String reportNamePrefix, String displayName) {
		this.reportNamePrefix = reportNamePrefix;
		this.displayName = displayName;
	}
	
	public String getReportNamePrefix() {
		return reportNamePrefix;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}