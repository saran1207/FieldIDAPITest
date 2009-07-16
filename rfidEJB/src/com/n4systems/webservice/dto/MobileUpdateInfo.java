package com.n4systems.webservice.dto;

public class MobileUpdateInfo {
	
	private int majorVersion;
	private int minorVersion;
	private int buildVersion;
	private String fileName;
		
	public int getMajorVersion() {
		return majorVersion;
	}
	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}
	public int getMinorVersion() {
		return minorVersion;
	}
	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}
	public int getBuildVersion() {
		return buildVersion;
	}
	public void setBuildVersion(int buildVersion) {
		this.buildVersion = buildVersion;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
