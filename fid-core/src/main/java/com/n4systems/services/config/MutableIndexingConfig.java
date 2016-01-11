package com.n4systems.services.config;

public class MutableIndexingConfig extends IndexingConfig {

	public void setAssetIndexEnabled(Boolean assetIndexEnabled) {
		this.assetIndexEnabled = assetIndexEnabled;
	}

	public void setAssetIndexSize(Integer assetIndexSize) {
		this.assetIndexSize = assetIndexSize;
	}

	public void setEventIndexEnabled(Boolean eventIndexEnabled) {
		this.eventIndexEnabled = eventIndexEnabled;
	}

	public void setEventIndexSize(Integer eventIndexSize) {
		this.eventIndexSize = eventIndexSize;
	}

	public void setTrendsIndexEnabled(Boolean trendsIndexEnabled) {
		this.trendsIndexEnabled = trendsIndexEnabled;
	}

	public void setTrendsIndexSize(Integer trendsIndexSize) {
		this.trendsIndexSize = trendsIndexSize;
	}
}
