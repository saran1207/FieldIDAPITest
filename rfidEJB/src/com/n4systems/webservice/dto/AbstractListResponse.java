package com.n4systems.webservice.dto;

import com.n4systems.tools.Pager;

public abstract class AbstractListResponse extends RequestResponse {

	private int currentPage;
	private int recordsPerPage;
	private int totalPages;

	public AbstractListResponse() {
	}

	public AbstractListResponse(Pager<?> page, int pageSize) {
		currentPage = page.getCurrentPage();
		recordsPerPage = pageSize;
		totalPages = (int) page.getTotalPages();
	}

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
