package com.n4systems.webservice.dto;

public class CompletedJobScheduleRequest extends RequestInformation {

	private String inspectionMobileGuid;
	private String nextDate;
	private long jobId;
	
	public String getInspectionMobileGuid() {
		return inspectionMobileGuid;
	}
	public void setInspectionMobileGuid(String inspectionMobileGuid) {
		this.inspectionMobileGuid = inspectionMobileGuid;
	}
	public String getNextDate() {
		return nextDate;
	}
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
}
