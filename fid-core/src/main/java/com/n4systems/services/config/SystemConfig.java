package com.n4systems.services.config;

public class SystemConfig {
	protected String nodeName;
	protected String domain;
	protected String protocol;
	protected String systemUserAddress;
	protected String systemUserPassword;
	protected String systemUserUserId;
	protected String houseAccountName;
	protected Long houseAccountPrimaryOrgId;
	protected String globalApplicationRoot;
	protected Integer defaultExecutorPoolSize;
	protected Integer hourToRunEventSchedNotifications;
	protected Integer downloadTTLDays;
	protected String unbrandedSubdomain;
	protected String defaultProductTypeName;
	protected String defaultTimezoneId;

	public SystemConfig() {}

	public SystemConfig(SystemConfig other) {
		this.nodeName = other.nodeName;
		this.domain = other.domain;
		this.protocol = other.protocol;
		this.systemUserAddress = other.systemUserAddress;
		this.systemUserPassword = other.systemUserPassword;
		this.systemUserUserId = other.systemUserUserId;
		this.houseAccountName = other.houseAccountName;
		this.houseAccountPrimaryOrgId = other.houseAccountPrimaryOrgId;
		this.globalApplicationRoot = other.globalApplicationRoot;
		this.defaultExecutorPoolSize = other.defaultExecutorPoolSize;
		this.hourToRunEventSchedNotifications = other.hourToRunEventSchedNotifications;
		this.downloadTTLDays = other.downloadTTLDays;
		this.unbrandedSubdomain = other.unbrandedSubdomain;
		this.defaultProductTypeName = other.defaultProductTypeName;
		this.defaultTimezoneId = other.defaultTimezoneId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getDomain() {
		return domain;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getSystemUserAddress() {
		return systemUserAddress;
	}

	public String getSystemUserPassword() {
		if (systemUserPassword == null || systemUserPassword.length() < 128) {
			throw new SecurityException("System password not configured correctly");
		}
		return systemUserPassword;
	}

	public String getSystemUserUserId() {
		return systemUserUserId;
	}

	public String getHouseAccountName() {
		return houseAccountName;
	}

	public Long getHouseAccountPrimaryOrgId() {
		return houseAccountPrimaryOrgId;
	}

	public String getGlobalApplicationRoot() {
		return globalApplicationRoot;
	}

	public Integer getDefaultExecutorPoolSize() {
		return defaultExecutorPoolSize;
	}

	public Integer getHourToRunEventSchedNotifications() {
		return hourToRunEventSchedNotifications;
	}

	public Integer getDownloadTTLDays() {
		return downloadTTLDays;
	}

	public String getUnbrandedSubdomain() {
		return unbrandedSubdomain;
	}

	public String getDefaultProductTypeName() {
		return defaultProductTypeName;
	}

	public String getDefaultTimezoneId() {
		return defaultTimezoneId;
	}

	@Override
	public String toString() {
		return  "\t\tnodeName: '" + nodeName + "'\n" +
				"\t\tdomain: '" + domain + "'\n" +
				"\t\tprotocol: '" + protocol + "'\n" +
				"\t\tsystemUserAddress: '" + systemUserAddress + "'\n" +
				"\t\tsystemUserPassword: '" + systemUserPassword + "'\n" +
				"\t\tsystemUserUserId: '" + systemUserUserId + "'\n" +
				"\t\thouseAccountName: '" + houseAccountName + "'\n" +
				"\t\thouseAccountPrimaryOrgId: " + houseAccountPrimaryOrgId + '\n' +
				"\t\tglobalApplicationRoot: '" + globalApplicationRoot + "'\n" +
				"\t\tdefaultExecutorPoolSize: " + defaultExecutorPoolSize + '\n' +
				"\t\thourToRunEventSchedNotifications: " + hourToRunEventSchedNotifications + '\n' +
				"\t\tdownloadTTLDays: " + downloadTTLDays + '\n' +
				"\t\tunbrandedSubdomain: '" + unbrandedSubdomain + "'\n" +
				"\t\tdefaultProductTypeName: '" + defaultProductTypeName + "'\n" +
				"\t\tdefaultTimezoneId: '" + defaultTimezoneId + "'\n";
	}
}
