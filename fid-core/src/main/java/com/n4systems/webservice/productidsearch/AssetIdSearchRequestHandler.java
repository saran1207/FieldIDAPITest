package com.n4systems.webservice.productidsearch;

import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.model.asset.SyncAssetListLoader;
import com.n4systems.webservice.RequestHandler;
import com.n4systems.webservice.assetdownload.AssetIdListResponse;
import com.n4systems.webservice.assetdownload.AssetSearchRequest;
import com.n4systems.webservice.assetdownload.SyncAsset;
import com.n4systems.webservice.exceptions.ServiceException;

public class AssetIdSearchRequestHandler implements RequestHandler<AssetSearchRequest, AssetIdListResponse> {
	private final Logger logger = Logger.getLogger(AssetIdSearchRequestHandler.class);
	private final SyncAssetListLoader assetIdLoader;
	
	public AssetIdSearchRequestHandler(SyncAssetListLoader productIdLoader) {
		this.assetIdLoader = productIdLoader;
	}
	
	@Override
	public AssetIdListResponse getResponse(AssetSearchRequest request) throws ServiceException {
		AssetIdListResponse response = new AssetIdListResponse();
		
		try {
			setupProductIdLoader(request);
			
			List<SyncAsset> assetIds = assetIdLoader.load();
			response.setAssets(assetIds);
			
		} catch(Exception e) {
			logger.error("Unable to handle AssetSearchRequest", e);
			throw new ServiceException(e.getMessage());
		}
		
		return response;
	}

	private void setupProductIdLoader(AssetSearchRequest request) {
		assetIdLoader.getOwnerIds().addAll(request.getOwnerIds());
		assetIdLoader.getLocationIds().addAll(request.getLocationIds());
		assetIdLoader.getJobIds().addAll(request.getJobIds());
	}
}
