package com.n4systems.services.config;

public class IndexingConfig {
	protected Boolean assetIndexEnabled;
	protected Integer assetIndexSize;
	protected Boolean eventIndexEnabled;
	protected Integer eventIndexSize;
	protected Boolean trendsIndexEnabled;
	protected Integer trendsIndexSize;

	public IndexingConfig() {}

	public IndexingConfig(IndexingConfig orig) {
		this.assetIndexEnabled = orig.assetIndexEnabled;
		this.assetIndexSize = orig.assetIndexSize;
		this.eventIndexEnabled = orig.eventIndexEnabled;
		this.eventIndexSize = orig.eventIndexSize;
		this.trendsIndexEnabled = orig.trendsIndexEnabled;
		this.trendsIndexSize = orig.trendsIndexSize;
	}

	public Boolean getAssetIndexEnabled() {
		return assetIndexEnabled;
	}

	public Integer getAssetIndexSize() {
		return assetIndexSize;
	}

	public Boolean getEventIndexEnabled() {
		return eventIndexEnabled;
	}

	public Integer getEventIndexSize() {
		return eventIndexSize;
	}

	public Boolean getTrendsIndexEnabled() {
		return trendsIndexEnabled;
	}

	public Integer getTrendsIndexSize() {
		return trendsIndexSize;
	}

	@Override
	public String toString() {
		return "\t\tassetIndexEnabled: " + assetIndexEnabled + '\n' +
				"\t\tassetIndexSize: " + assetIndexSize + '\n' +
				"\t\teventIndexEnabled: " + eventIndexEnabled + '\n' +
				"\t\teventIndexSize: " + eventIndexSize + '\n' +
				"\t\ttrendsIndexEnabled: " + trendsIndexEnabled + '\n' +
				"\t\ttrendsIndexSize: " + trendsIndexSize + '\n';
	}
}
