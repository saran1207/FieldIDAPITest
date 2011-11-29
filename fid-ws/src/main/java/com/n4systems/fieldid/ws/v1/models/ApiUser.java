package com.n4systems.fieldid.ws.v1.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApiUser extends ApiReadonlyModelWithOwner {
	private String userId;
	private String name;
	private String hashPassword;
	private String hashSecurityCardNumber;
	private String authKey;
	private String userType;
	private ApiUserPermissions permissions = new ApiUserPermissions();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	public String getHashSecurityCardNumber() {
		return hashSecurityCardNumber;
	}

	public void setHashSecurityCardNumber(String hashSecurityCardNumber) {
		this.hashSecurityCardNumber = hashSecurityCardNumber;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public ApiUserPermissions getPermissions() {
		return permissions;
	}

	public void setPermissions(ApiUserPermissions permissions) {
		this.permissions = permissions;
	}

}
