package com.n4systems.webservice;

import com.n4systems.model.Product;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.webservice.assetdownload.AssetListResponse;
import com.n4systems.webservice.assetdownload.AssetRequest;
import com.n4systems.webservice.assetdownload.GetAssetsRequestHandler;
import com.n4systems.webservice.dto.PaginatedRequestInformation;
import com.n4systems.webservice.dto.AssetIdListResponse;
import com.n4systems.webservice.dto.AssetSearchRequest;
import com.n4systems.webservice.predefinedlocation.AllPredefinedLocationsRequestHandler;
import com.n4systems.webservice.predefinedlocation.PredefinedLocationListResponse;
import com.n4systems.webservice.productidsearch.AssetIdSearchRequestHandler;

public class RequestHandlerFactory {
	private final ConfigContext configContext;
	private final LoaderFactory loaderFactory;
	private final ModelToServiceConverterFactory converterFactory;
	
	public RequestHandlerFactory(ConfigContext configContext, LoaderFactory loaderFactory, ModelToServiceConverterFactory converterFactory) {
		this.configContext = configContext;
		this.loaderFactory = loaderFactory;
		this.converterFactory = converterFactory;
	}
	
	public RequestHandler<PaginatedRequestInformation, PredefinedLocationListResponse> createAllPredefinedLocationsRequestHandler() {
		return new AllPredefinedLocationsRequestHandler(configContext, loaderFactory, converterFactory.createPredefinedLocationToServiceConverter());
	}
	
	public RequestHandler<AssetSearchRequest, AssetIdListResponse> createAssetIdSearchRequestHandler() {
		return new AssetIdSearchRequestHandler(loaderFactory.createProductIdSearchListLoader());
	}
	
	public RequestHandler<AssetRequest, AssetListResponse> createGetAssetRequestHandler() {
		return new GetAssetsRequestHandler(loaderFactory.createFilteredInListLoader(Product.class), converterFactory.createProductToServiceConverter());
	}
}
