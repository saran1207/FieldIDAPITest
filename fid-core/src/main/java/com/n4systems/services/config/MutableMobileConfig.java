package com.n4systems.services.config;

public class MutableMobileConfig extends MobileConfig {

	public void setCurrentMobileFileName(String currentMobileFileName) {
		this.currentMobileFileName = currentMobileFileName;
	}

	public void setCurrentMobileMajorVersion(Integer currentMobileMajorVersion) {
		this.currentMobileMajorVersion = currentMobileMajorVersion;
	}

	public void setCurrentMobileMinorVersion(Integer currentMobileMinorVersion) {
		this.currentMobileMinorVersion = currentMobileMinorVersion;
	}

	public void setCurrentMobileBuildVersion(Integer currentMobileBuildVersion) {
		this.currentMobileBuildVersion = currentMobileBuildVersion;
	}

	public void setMinimumMobileMajorVersion(Integer minimumMobileMajorVersion) {
		this.minimumMobileMajorVersion = minimumMobileMajorVersion;
	}

	public void setMinimumMobileMinorVersion(Integer minimumMobileMinorVersion) {
		this.minimumMobileMinorVersion = minimumMobileMinorVersion;
	}

	public void setMobliePagesizeSetupdata(Integer mobliePagesizeSetupdata) {
		this.mobliePagesizeSetupdata = mobliePagesizeSetupdata;
	}

	public void setMobilePagesizeInspections(String mobilePagesizeInspections) {
		this.mobilePagesizeInspections = mobilePagesizeInspections;
	}

	public void setMobilePagesizeProducts(String mobilePagesizeProducts) {
		this.mobilePagesizeProducts = mobilePagesizeProducts;
	}

}
