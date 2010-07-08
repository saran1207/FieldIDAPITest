package com.n4systems.fieldid.actions.product;

import com.n4systems.fieldid.actions.api.LoaderFactoryProvider;
import com.n4systems.model.Product;

public class AssetWebModel {
	private final LocationWebModel location;

	public AssetWebModel(LoaderFactoryProvider factoryProvider) {
		super();
		this.location = new LocationWebModel(factoryProvider);
	}
	
	public AssetWebModel match(Product product) {
		location.matchLocation(product.getAdvancedLocation());
		return this;
	}
	

	public LocationWebModel getLocation() {
		return location;
	}
	
	public Product fillInAsset(Product asset) {
		asset.setAdvancedLocation(location.createLocation());
		return asset;
	}
	
	
}
