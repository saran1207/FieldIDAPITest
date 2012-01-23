package com.n4systems.fieldid.ws.v1.resources.user;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModelWithOwner;
import com.n4systems.fieldid.ws.v1.resources.offlineprofile.ApiOfflineProfile;

public class ApiUser extends ApiReadonlyModelWithOwner {
	private String userId;
	private String name;
	private String authKey;
	private String hashPassword;
	private String hashSecurityCardNumber;
	private String userType;
	private boolean identifyEnabled;
	private boolean createEventEnabled;
	private boolean editEventEnabled;
	private ApiOfflineProfile offlineProfile;
	
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

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String apiKey) {
		this.authKey = apiKey;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public boolean isIdentifyEnabled() {
		return identifyEnabled;
	}

	public void setIdentifyEnabled(boolean permissionIdentify) {
		this.identifyEnabled = permissionIdentify;
	}

	public boolean isCreateEventEnabled() {
		return createEventEnabled;
	}

	public void setCreateEventEnabled(boolean permissionCreateEvent) {
		this.createEventEnabled = permissionCreateEvent;
	}

	public boolean isEditEventEnabled() {
		return editEventEnabled;
	}

	public void setEditEventEnabled(boolean permissionEditEvent) {
		this.editEventEnabled = permissionEditEvent;
	}

	public ApiOfflineProfile getOfflineProfile() {
		return offlineProfile;
	}

	public void setOfflineProfile(ApiOfflineProfile offlineProfile) {
		this.offlineProfile = offlineProfile;
	}

}
