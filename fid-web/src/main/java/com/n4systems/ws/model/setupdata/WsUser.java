package com.n4systems.ws.model.setupdata;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsUser extends WsModel {
	
	private String userId;
	private String hashPassword;
	private String hashSecurityCardNumber;
	private boolean allowedToIdentify;
	private boolean allowedToInspect;
	private boolean attachedToPrimaryOrg;
	private long ownerId;	
	private String firstName;
	private String lastName;
	private boolean deleted;
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@XmlElement(name="UserId")
	public String getUserId() {
		return userId;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	@XmlElement(name="HashPassword")
	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashSecurityCardNumber(String hashSecurityCardNumber) {
		this.hashSecurityCardNumber = hashSecurityCardNumber;
	}

	@XmlElement(name="HashSecurityCardNumber")
	public String getHashSecurityCardNumber() {
		return hashSecurityCardNumber;
	}

	public void setAllowedToIdentify(boolean allowedToIdentify) {
		this.allowedToIdentify = allowedToIdentify;
	}

	@XmlElement(name="AllowedToIdentify")
	public boolean isAllowedToIdentify() {
		return allowedToIdentify;
	}

	public void setAllowedToInspect(boolean allowedToInspect) {
		this.allowedToInspect = allowedToInspect;
	}

	@XmlElement(name="AllowedToInspect")
	public boolean isAllowedToInspect() {
		return allowedToInspect;
	}

	public void setAttachedToPrimaryOrg(boolean attachedToPrimaryOrg) {
		this.attachedToPrimaryOrg = attachedToPrimaryOrg;
	}

	@XmlElement(name="AttachedToPrimaryOrg")
	public boolean isAttachedToPrimaryOrg() {
		return attachedToPrimaryOrg;
	}
	
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	
	@XmlElement(name="OwnerId")
	public long getOwnerId() {
		return ownerId;
	}
		
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement(name="FirstName")
	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlElement(name="LastName")
	public String getLastName() {
		return lastName;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@XmlElement(name="Deleted")
	public boolean isDeleted() {
		return deleted;
	}	
}
