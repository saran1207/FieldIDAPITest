package com.n4systems.webservice.dto;

public class UserServiceDTO extends AbstractBaseServiceDTO {

	private String userId;
	private String hashPassword;
	private String securityRfidNumber;
	private long customerId;
	private long divisionId;
	private boolean allowedToIdentify;
	private boolean allowedToInspect;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHashPassword() {
		return hashPassword;
	}
	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public boolean isAllowedToIdentify() {
		return allowedToIdentify;
	}
	public void setAllowedToIdentify(boolean allowedToIdentify) {
		this.allowedToIdentify = allowedToIdentify;
	}
	public boolean isAllowedToInspect() {
		return allowedToInspect;
	}
	public void setAllowedToInspect(boolean allowedToInspect) {
		this.allowedToInspect = allowedToInspect;
	}
	public long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}
	public String getSecurityRfidNumber() {
		return securityRfidNumber;
	}
	public void setSecurityRfidNumber(String securityRfidNumber) {
		this.securityRfidNumber = securityRfidNumber;
	}		
}
