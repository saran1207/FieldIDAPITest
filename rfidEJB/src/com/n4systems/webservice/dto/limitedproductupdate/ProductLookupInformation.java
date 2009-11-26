package com.n4systems.webservice.dto.limitedproductupdate;

import com.n4systems.webservice.dto.MobileDTOHelper;
import com.n4systems.webservice.dto.ProductLookupable;

public class ProductLookupInformation implements ProductLookupable {
	private String mobileGuid;
	private Long id;
	private String serialNumber;
	private String rfidNumber;

	public String getMobileGuid() {
		return mobileGuid;
	}

	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}
	
	public boolean isCreatedOnMobile() {
		return MobileDTOHelper.isValidServerId(id);
	}
}