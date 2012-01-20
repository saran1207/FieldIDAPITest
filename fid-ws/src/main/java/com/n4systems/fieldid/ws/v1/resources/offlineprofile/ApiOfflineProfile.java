package com.n4systems.fieldid.ws.v1.resources.offlineprofile;

import java.util.ArrayList;
import java.util.List;

public class ApiOfflineProfile {
	private Long userId;
	private List<String> asset = new ArrayList<String>();
	private List<Long> organization = new ArrayList<Long>();

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getAsset() {
		return asset;
	}

	public void setAsset(List<String> asset) {
		this.asset = asset;
	}

	public List<Long> getOrganization() {
		return organization;
	}

	public void setOrganization(List<Long> organization) {
		this.organization = organization;
	}

}
