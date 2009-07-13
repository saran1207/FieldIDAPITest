package com.n4systems.webservice.dto;

/**
 * XXX - Lets remove this class and put modified into PaginatedRequestInformation to avoid more classes on 
 *       the handheld side.  We can also use Date modified versus String.  Bam. 
 */
@Deprecated // In 2009.4
public class PaginatedUpdateRequestInfo extends PaginatedRequestInformation {

	private String modified;

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}
}
