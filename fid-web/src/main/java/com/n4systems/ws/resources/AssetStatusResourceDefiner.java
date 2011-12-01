package com.n4systems.ws.resources;


import com.n4systems.model.AssetStatus;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.assettype.WsAssetStatus;
import com.n4systems.ws.model.assettype.WsAssetStatusConverter;

public class AssetStatusResourceDefiner implements ResourceDefiner<AssetStatus, WsAssetStatus> {

	@Override
	public WsModelConverter<AssetStatus, WsAssetStatus> getResourceConverter() {
		return new WsAssetStatusConverter();
	}

	@Override
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(AssetStatus.class);
	}

	@Override
	public IdLoader<? extends Loader<AssetStatus>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(AssetStatus.class);
	}

	@Override
	public Class<WsAssetStatus> getWsModelClass() {
		return WsAssetStatus.class;
	}

}
