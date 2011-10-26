package com.n4systems.ws.model.setupdata;

import static com.n4systems.webservice.dto.MobileDTOHelper.isValidServerId;

import com.n4systems.ws.model.WsModel;

public class WsUser extends WsModel {
	
	private String userId;
	private String hashPassword;
	private String hashSecurityCardNumber;
	private boolean allowedToIdentify;
	private boolean allowedToInspect;
	private boolean attachedToPrimaryOrg;
	private long ownerId;
	
	//TODO Remove the following 3 fields here and in mobile.
	private long customerId;
	private long divisionId;
	private long orgId;
	
	private String firstName;
	private String lastName;
	private boolean deleted;
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashSecurityCardNumber(String hashSecurityCardNumber) {
		this.hashSecurityCardNumber = hashSecurityCardNumber;
	}

	public String HashSecurityCardNumber() {
		return hashSecurityCardNumber;
	}

	public void setAllowedToIdentify(boolean allowedToIdentify) {
		this.allowedToIdentify = allowedToIdentify;
	}

	public boolean isAllowedToIdentify() {
		return allowedToIdentify;
	}

	public void setAllowedToInspect(boolean allowedToInspect) {
		this.allowedToInspect = allowedToInspect;
	}

	public boolean isAllowedToInspect() {
		return allowedToInspect;
	}

	public void setAttachedToPrimaryOrg(boolean attachedToPrimaryOrg) {
		this.attachedToPrimaryOrg = attachedToPrimaryOrg;
	}

	public boolean isAttachedToPrimaryOrg() {
		return attachedToPrimaryOrg;
	}
	
	public long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	
	public boolean ownerIdExists() {
		return isValidServerId(ownerId);
	}
	
	public long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
	public boolean customerExists() {
		return isValidServerId( customerId );
	}
	
	public long getDivisionId() {
		return divisionId;
	}
	
	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}
	
	public boolean divisionExists() {
		return isValidServerId( divisionId );
	}
	
	public long getOrgId() {
		return orgId;
	}
	
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}	
}
