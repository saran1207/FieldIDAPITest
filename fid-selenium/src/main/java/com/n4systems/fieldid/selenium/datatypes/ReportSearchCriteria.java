package com.n4systems.fieldid.selenium.datatypes;

public class ReportSearchCriteria extends AssetSearchCriteria {
	
	private String job;
	
	private String performedBy;
	
	private String eventTypeGroup;
	
	private String eventBook;
	
	private boolean safetyNetworkResults;
	
	private String result;

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

	public String getEventTypeGroup() {
		return eventTypeGroup;
	}

	public void setEventTypeGroup(String eventTypeGroup) {
		this.eventTypeGroup = eventTypeGroup;
	}

	public String getEventBook() {
		return eventBook;
	}

	public void setEventBook(String eventBook) {
		this.eventBook = eventBook;
	}

	public boolean getSafetyNetworkResults() {
		return safetyNetworkResults;
	}

	public void setSafetyNetworkResults(boolean safetyNetworkResults) {
		this.safetyNetworkResults = safetyNetworkResults;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
