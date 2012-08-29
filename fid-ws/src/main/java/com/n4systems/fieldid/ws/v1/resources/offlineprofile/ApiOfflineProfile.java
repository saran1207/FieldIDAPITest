package com.n4systems.fieldid.ws.v1.resources.offlineprofile;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;

public class ApiOfflineProfile {
	private Long userId;
	private SyncDuration syncDuration;
	private List<String> assets = new ArrayList<String>();
	private List<Long> organizations = new ArrayList<Long>();	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public SyncDuration getSyncDuration() {
		return syncDuration;
	}

	public void setSyncDuration(SyncDuration syncDuration) {
		this.syncDuration = syncDuration;
	}

	public List<String> getAssets() {
		return assets;
	}

	public void setAssets(List<String> asset) {
		this.assets = asset;
	}

	public List<Long> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Long> organization) {
		this.organizations = organization;
	}
}
