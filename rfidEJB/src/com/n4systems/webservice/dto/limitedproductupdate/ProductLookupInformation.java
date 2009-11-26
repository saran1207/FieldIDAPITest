package com.n4systems.webservice.dto.limitedproductupdate;

public class ProductLookupInformation {
	private String productMobileGuid;
	private long productId;
	private String serialNumber;
	private String rfidNumber;

	public ProductLookupInformation() {
	}

	public String getProductMobileGuid() {
		return productMobileGuid;
	}

	public void setProductMobileGuid(String productMobileGuid) {
		this.productMobileGuid = productMobileGuid;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
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
}