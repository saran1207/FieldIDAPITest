package com.n4systems.webservice.dto.findinspection;

import com.n4systems.webservice.dto.RequestInformation;

public class FindInspectionRequestInformation extends RequestInformation {
	
	private long productId;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}
}
