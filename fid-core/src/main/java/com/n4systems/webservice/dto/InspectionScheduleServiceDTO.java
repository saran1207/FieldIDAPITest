package com.n4systems.webservice.dto;

public class InspectionScheduleServiceDTO extends AbstractBaseServiceDTO {
	
	private String nextDate;
	private long productId;
	private String productMobileGuid;
	private long inspectionTypeId;
	private long jobId;
	private boolean completed;
	private String mobileGuid;

	public String getNextDate() {
		return nextDate;
	}
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getInspectionTypeId() {
		return inspectionTypeId;
	}
	public void setInspectionTypeId(long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getProductMobileGuid() {
		return productMobileGuid;
	}
	public void setProductMobileGuid(String productMobileGuid) {
		this.productMobileGuid = productMobileGuid;
	}
	public boolean isProductCreatedOnMobile() {
		return !MobileDTOHelper.isValidServerId(productId);
	}
	public String getMobileGuid() {
		return mobileGuid;
	}
	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}
	
	
	
}
