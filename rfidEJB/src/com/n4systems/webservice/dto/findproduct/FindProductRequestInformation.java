package com.n4systems.webservice.dto.findproduct;

import com.n4systems.webservice.dto.RequestInformation;

public class FindProductRequestInformation extends RequestInformation {

	private String searchText;

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}
