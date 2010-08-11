package com.n4systems.webservice.assetdownload;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.webservice.dto.RequestInformation;

public class AssetSearchRequest extends RequestInformation {
	private List<Long> ownerIds = new ArrayList<Long>();
	private List<Long> jobIds = new ArrayList<Long>();
	private List<Long> locationIds = new ArrayList<Long>();

	public List<Long> getOwnerIds() {
		return ownerIds;
	}

	public void setOwnerIds(List<Long> ownerIds) {
		this.ownerIds = ownerIds;
	}

	public List<Long> getJobIds() {
		return jobIds;
	}

	public void setJobIds(List<Long> jobIds) {
		this.jobIds = jobIds;
	}

	public List<Long> getLocationIds() {
		return locationIds;
	}

	public void setLocationIds(List<Long> locationIds) {
		this.locationIds = locationIds;
	}
}
