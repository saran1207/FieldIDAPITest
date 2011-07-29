package com.n4systems.webservice;

import com.n4systems.model.Asset;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.webservice.assetdownload.AssetIdListResponse;
import com.n4systems.webservice.assetdownload.AssetListResponse;
import com.n4systems.webservice.assetdownload.AssetRequest;
import com.n4systems.webservice.assetdownload.AssetSearchRequest;
import com.n4systems.webservice.assetdownload.GetAssetsRequestHandler;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.predefinedlocation.AllPredefinedLocationsRequestHandler;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationListResponse;
import com.n4systems.webservice.productidsearch.AssetIdSearchRequestHandler;

public class RequestHandlerFactory {
	private final ConfigurationProvider configContext;
	private final LoaderFactory loaderFactory;
	private final ModelToServiceConverterFactory converterFactory;
	
	public RequestHandlerFactory(ConfigurationProvider configContext, LoaderFactory loaderFactory, ModelToServiceConverterFactory converterFactory) {
		this.configContext = configContext;
		this.loaderFactory = loaderFactory;
		this.converterFactory = converterFactory;
	}
	
	public RequestHandler<PaginatedRequestInformation, PredefinedLocationListResponse> createAllPredefinedLocationsRequestHandler() {
		return new AllPredefinedLocationsRequestHandler(configContext, loaderFactory, converterFactory.createPredefinedLocationToServiceConverter());
	}
	
	public RequestHandler<AssetSearchRequest, AssetIdListResponse> createAssetIdSearchRequestHandler() {
		return new AssetIdSearchRequestHandler(loaderFactory.createAssetIdSearchListLoader());
	}
	
	public RequestHandler<AssetRequest, AssetListResponse> createGetAssetRequestHandler() {
		return new GetAssetsRequestHandler(loaderFactory.createFilteredInListLoader(Asset.class), converterFactory.createAssetToServiceConverter());
	}
}
