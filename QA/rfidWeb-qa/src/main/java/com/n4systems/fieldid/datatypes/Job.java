package com.n4systems.fieldid.datatypes;

public class Job {

	String jobID;
	String title;
	String jobType = event;
	String customer = null;
	String division = null;
	String status = null;
	boolean open = true;
	String description = null;
	String dateStarted = null;
	String dateEstimateCompleted = null;
	String dateActualCompleted = null;
	String duration = null;
	String poNumber = null;
	String workPerformed = null;

	public final static String event = "Event Job";
	public final static String asset = "Asset Job";
	
	public Job(String jobID, String jobTitle) {
		this.jobID = jobID;
		title = jobTitle;
	}
	
	public Job(String jobID, String jobTitle, String jobType) {
		this(jobID, jobTitle);
		this.jobType = jobType;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	
	public void setTitle(String jobTitle) {
		this.title = jobTitle;
	}
	
	public void setJobType(String s) {
		this.jobType = s;
	}
	
	public void setCustomer(String s) {
		this.customer = s;
	}
	
	public void setDivision(String s) {
		this.division = s;
	}
	
	public void setStatus(String s) {
		this.status = s;
	}
	
	public void setOpen(boolean b) {
		this.open = b;
	}
	
	public void setDescription(String s) {
		this.description = s;
	}
	
	public void setDateStarted(String s) {
		this.dateStarted = s;
	}
	
	public void setDateEstimateCompleted(String s) {
		this.dateEstimateCompleted = s;
	}
	
	public void setDateActualCompleted(String s) {
		this.dateActualCompleted = s;
	}
	
	public void setDuration(String s) {
		this.duration = s;
	}
	
	public void setPONumber(String s) {
		this.poNumber = s;
	}
	
	public void setWorkPerformed(String s) {
		this.workPerformed = s;
	}

	public String getJobID() {
		return this.jobID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getJobType() {
		return this.jobType;
	}
	
	public String getCustomer() {
		return this.customer;
	}
	
	public String getDivision() {
		return this.division;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public boolean getOpen() {
		return this.open;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getDateStarted() {
		return this.dateStarted;
	}
	
	public String getDateEstimateCompleted() {
		return this.dateEstimateCompleted;
	}
	
	public String getDateActualCompleted() {
		return this.dateActualCompleted;
	}
	
	public String getDuration() {
		return this.duration;
	}
	
	public String getPONumber() {
		return this.poNumber;
	}
	
	public String getWorkPerformed() {
		return this.workPerformed;
	}
}
