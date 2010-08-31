package com.n4systems.webservice.assetdownload;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.webservice.dto.RequestInformation;

public class AssetRequest extends RequestInformation {
	private boolean withPreviousEvents = false;
	private List<Long> assetIds = new ArrayList<Long>();

	public List<Long> getAssetIds() {
		return assetIds;
	}

	public void setAssetIds(List<Long> assetIds) {
		this.assetIds = assetIds;
	}

	public void setWithPreviousEvents(boolean withPreviousEvents) {
		this.withPreviousEvents = withPreviousEvents;
	}

	public boolean isWithPreviousEvents() {
		return withPreviousEvents;
	}
}
