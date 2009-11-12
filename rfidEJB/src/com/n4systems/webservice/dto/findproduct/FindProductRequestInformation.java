package com.n4systems.webservice.dto.findproduct;

import java.util.Date;

import com.n4systems.webservice.dto.RequestInformation;

public class FindProductRequestInformation extends RequestInformation {

	private String searchText;
	private Date modified;

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
}
