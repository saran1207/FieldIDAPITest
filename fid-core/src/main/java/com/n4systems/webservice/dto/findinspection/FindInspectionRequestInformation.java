package com.n4systems.webservice.dto.findinspection;

import com.n4systems.webservice.dto.RequestInformation;

import java.util.Date;

public class FindInspectionRequestInformation extends RequestInformation {
	
	private long productId;
	private Date lastInspectionDate;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public Date getLastInspectionDate() {
		return lastInspectionDate;
	}

	public void setLastInspectionDate(Date lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}
}
