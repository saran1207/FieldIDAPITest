package com.n4systems.webservice.dto;

public abstract class AbstractListResponse extends RequestResponse {

	private int currentPage;
	private int recordsPerPage;
	private int totalPages;
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getRecordsPerPage() {
		return recordsPerPage;
	}
	public void setRecordsPerPage(int recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
}
