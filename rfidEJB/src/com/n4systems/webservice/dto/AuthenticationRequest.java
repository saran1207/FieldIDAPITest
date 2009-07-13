package com.n4systems.webservice.dto;

public class AuthenticationRequest {
	
	public static enum LoginType { USERNAME, SECURITY };

	private String tenantName;
	private String userId;
	private String password;
	private String securityRfidNumber;
	private long majorVersion;
	private long minorVersion;
	private LoginType loginType;

	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecurityRfidNumber() {
		return securityRfidNumber;
	}
	public void setSecurityRfidNumber(String securityRfidNumber) {
		this.securityRfidNumber = securityRfidNumber;
	}
	public long getMajorVersion() {
		return majorVersion;
	}
	public void setMajorVersion(long majorVersion) {
		this.majorVersion = majorVersion;
	}
	public long getMinorVersion() {
		return minorVersion;
	}
	public void setMinorVersion(long minorVersion) {
		this.minorVersion = minorVersion;
	}
	public LoginType getLoginType() {
		return loginType;
	}
	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

}
