package com.n4systems.fieldid.ws.v2.resources.user;

import com.n4systems.fieldid.ws.v2.offlineprofile.ApiOfflineProfile;
import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModelWithOwner;
import com.n4systems.fieldid.ws.v2.resources.tenant.ApiTenant;
import com.n4systems.security.UserType;

import java.util.List;

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
	private ApiTenant tenant;
	private String identifier;
	private List<Long> groupIds;
	
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
	
	public ApiTenant getTenant() {
		return tenant;
	}

	public void setTenant(ApiTenant tenant) {
		this.tenant = tenant;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
    public boolean isPerson() {
        return userType.equals(UserType.PERSON.name());
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public Long getGroupId() {
        if (groupIds == null || groupIds.isEmpty()) {
            return null;
        }
        return groupIds.get(0);
    }

}
