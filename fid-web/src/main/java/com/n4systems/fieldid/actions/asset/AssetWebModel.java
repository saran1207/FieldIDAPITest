package com.n4systems.fieldid.actions.asset;

import com.n4systems.fieldid.actions.api.LoaderFactoryProvider;
import com.n4systems.model.Asset;

public class AssetWebModel {
	private final LocationWebModel location;

	public AssetWebModel(LoaderFactoryProvider factoryProvider) {
		this.location = new LocationWebModel(factoryProvider);
	}
	
	public AssetWebModel match(Asset asset) {
		location.matchLocation(asset.getAdvancedLocation());
		return this;
	}
	

	public LocationWebModel getLocation() {
		return location;
	}
	
	public Asset fillInAsset(Asset asset) {
		asset.setAdvancedLocation(location.createLocation());
		return asset;
	}
	
	
}
