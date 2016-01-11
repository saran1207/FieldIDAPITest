package com.n4systems.services.config;

public class MobileConfig {
	protected String currentMobileFileName;
	protected Integer currentMobileMajorVersion;
	protected Integer currentMobileMinorVersion;
	protected Integer currentMobileBuildVersion;
	protected Integer minimumMobileMajorVersion;
	protected Integer minimumMobileMinorVersion;
	protected Integer mobliePagesizeSetupdata;
	protected String mobilePagesizeInspections;
	protected String mobilePagesizeProducts;

	public MobileConfig() {}

	public MobileConfig(MobileConfig other) {
		this.currentMobileFileName = other.currentMobileFileName;
		this.currentMobileMajorVersion = other.currentMobileMajorVersion;
		this.currentMobileMinorVersion = other.currentMobileMinorVersion;
		this.currentMobileBuildVersion = other.currentMobileBuildVersion;
		this.minimumMobileMajorVersion = other.minimumMobileMajorVersion;
		this.minimumMobileMinorVersion = other.minimumMobileMinorVersion;
		this.mobliePagesizeSetupdata = other.mobliePagesizeSetupdata;
		this.mobilePagesizeInspections = other.mobilePagesizeInspections;
		this.mobilePagesizeProducts = other.mobilePagesizeProducts;
	}

	public String getCurrentMobileFileName() {
		return currentMobileFileName;
	}

	public Integer getCurrentMobileMajorVersion() {
		return currentMobileMajorVersion;
	}

	public Integer getCurrentMobileMinorVersion() {
		return currentMobileMinorVersion;
	}

	public Integer getCurrentMobileBuildVersion() {
		return currentMobileBuildVersion;
	}

	public Integer getMinimumMobileMajorVersion() {
		return minimumMobileMajorVersion;
	}

	public Integer getMinimumMobileMinorVersion() {
		return minimumMobileMinorVersion;
	}

	public Integer getMobliePagesizeSetupdata() {
		return mobliePagesizeSetupdata;
	}

	public String getMobilePagesizeInspections() {
		return mobilePagesizeInspections;
	}

	public String getMobilePagesizeProducts() {
		return mobilePagesizeProducts;
	}

	@Override
	public String toString() {
		return "\t\tcurrentMobileFileName: '" + currentMobileFileName + "'\n" +
				"\t\tcurrentMobileMajorVersion: " + currentMobileMajorVersion + '\n' +
				"\t\tcurrentMobileMinorVersion: " + currentMobileMinorVersion + '\n' +
				"\t\tcurrentMobileBuildVersion: " + currentMobileBuildVersion + '\n' +
				"\t\tminimumMobileMajorVersion: " + minimumMobileMajorVersion + '\n' +
				"\t\tminimumMobileMinorVersion: " + minimumMobileMinorVersion + '\n' +
				"\t\tmobliePagesizeSetupdata: " + mobliePagesizeSetupdata + '\n' +
				"\t\tmobilePagesizeInspections: '" + mobilePagesizeInspections + "'\n" +
				"\t\tmobilePagesizeProducts: '" + mobilePagesizeProducts + "'\n";
	}
}
