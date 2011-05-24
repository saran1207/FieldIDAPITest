package com.n4systems.fieldid.selenium.lib;

import java.util.List;

public class PageErrorException extends RuntimeException {

	public PageErrorException(List<String> pageErrors) {
        super(pageErrors.toString());
	}

}
