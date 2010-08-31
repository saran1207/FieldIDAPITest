package com.n4systems.webservice.dto;

import com.n4systems.util.GUIDHelper;

/**
 * This service DTO packages up the information the client will send up, including their API version and tenant id.
 * 
 * @author Jesse Miller
 *
 */
public class RequestInformation {

	private long tenantId;
	private long versionNumber;
	private String mobileGuid;
	
	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}
	public long getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(long versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getMobileGuid() {
		return mobileGuid;
	}
	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}
	
	public boolean hasValidTransactionId() {
		return !GUIDHelper.isNullGUID( mobileGuid );
	}
	
}
