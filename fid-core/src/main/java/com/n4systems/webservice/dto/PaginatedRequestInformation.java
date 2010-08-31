package com.n4systems.webservice.dto;

public class PaginatedRequestInformation extends RequestInformation {
	
	/**
	 * When the information page is sent the response should contain no data but only the query totals (eg. total pages)
	 */
	public static Long INFORMATION_PAGE = 0L;
	
	private Long pageNumber;

	public Long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}

}
