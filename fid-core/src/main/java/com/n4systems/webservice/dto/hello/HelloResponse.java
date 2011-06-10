package com.n4systems.webservice.dto.hello;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class HelloResponse {
	private int majorVersion;
	private int minorVersion;
	private int buildVersion;

	public HelloResponse(ConfigContext context) {
		majorVersion = context.getInteger(ConfigEntry.CURRENT_MOBILE_MAJOR_VERSION);
		minorVersion = context.getInteger(ConfigEntry.CURRENT_MOBILE_MINOR_VERSION);
		buildVersion = context.getInteger(ConfigEntry.CURRENT_MOBILE_BUILD_VERSION);		
	}

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

}
