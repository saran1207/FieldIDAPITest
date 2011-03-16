package com.n4systems.fieldid.selenium.lib;

import java.util.List;

public class PageErrorException extends RuntimeException {
	private final List<String> pageErrors;
	
	public PageErrorException(List<String> pageErrors) {
		this.pageErrors = pageErrors;
	}

	public List<String> getPageErrors() {
		return pageErrors;
	}
		
}
