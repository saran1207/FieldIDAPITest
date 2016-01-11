package com.n4systems.services.config;


public class MutableSystemConfig extends SystemConfig {

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setSystemUserAddress(String systemUserAddress) {
		this.systemUserAddress = systemUserAddress;
	}

	public void setSystemUserPassword(String systemUserPassword) {
		this.systemUserPassword = systemUserPassword;
	}

	public void setSystemUserUserId(String systemUserUserId) {
		this.systemUserUserId = systemUserUserId;
	}

	public void setHouseAccountName(String houseAccountName) {
		this.houseAccountName = houseAccountName;
	}

	public void setHouseAccountPrimaryOrgId(Long houseAccountPrimaryOrgId) {
		this.houseAccountPrimaryOrgId = houseAccountPrimaryOrgId;
	}

	public void setGlobalApplicationRoot(String globalApplicationRoot) {
		this.globalApplicationRoot = globalApplicationRoot;
	}

	public void setDefaultExecutorPoolSize(Integer defaultExecutorPoolSize) {
		this.defaultExecutorPoolSize = defaultExecutorPoolSize;
	}

	public void setHourToRunEventSchedNotifications(Integer hourToRunEventSchedNotifications) {
		this.hourToRunEventSchedNotifications = hourToRunEventSchedNotifications;
	}

	public void setDownloadTTLDays(Integer downloadTTLDays) {
		this.downloadTTLDays = downloadTTLDays;
	}

	public void setUnbrandedSubdomain(String unbrandedSubdomain) {
		this.unbrandedSubdomain = unbrandedSubdomain;
	}

	public void setDefaultProductTypeName(String defaultProductTypeName) {
		this.defaultProductTypeName = defaultProductTypeName;
	}

	public void setDefaultTimezoneId(String defaultTimezoneId) {
		this.defaultTimezoneId = defaultTimezoneId;
	}
}
