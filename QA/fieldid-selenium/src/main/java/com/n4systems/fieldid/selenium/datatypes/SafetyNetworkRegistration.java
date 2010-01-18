package com.n4systems.fieldid.selenium.datatypes;

public class SafetyNetworkRegistration {
	String vendor;
	String assetNumber;	// can be serial number, RFID or reference number
	
	public SafetyNetworkRegistration(String vendor, String assetNumber) {
		this.vendor = vendor;
		this.assetNumber = assetNumber;
	}
	
	public void setVendor(String s) {
		vendor = s;
	}
	
	public void setAssetNumber(String s) {
		assetNumber = s;
	}
	
	public String getVendor() {
		return vendor;
	}
	
	public String getAssetNumber() {
		return assetNumber;
	}
}
