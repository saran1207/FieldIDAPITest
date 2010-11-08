package com.n4systems.webservice.assetdownload;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.webservice.asset.AssetToServiceConverter;
import org.apache.log4j.Logger;

import com.n4systems.persistence.loaders.FilteredInListLoader;
import com.n4systems.webservice.RequestHandler;
import com.n4systems.webservice.exceptions.ServiceException;

public class GetAssetsRequestHandler implements RequestHandler<AssetRequest, AssetListResponse>{
	private Logger logger = Logger.getLogger(GetAssetsRequestHandler.class);
	private static final int MAX_ASSET_REQUEST_SIZE = 2000;
	
	private final FilteredInListLoader<Asset> assetLoader;
	private final AssetToServiceConverter converter;
	
	public GetAssetsRequestHandler(FilteredInListLoader<Asset> assetLoader, AssetToServiceConverter converter) {
		this.assetLoader = assetLoader;
		this.converter = converter;
	}
	
	@Override
	public AssetListResponse getResponse(AssetRequest request) throws ServiceException {
		converter.setWithPreviousEvents(request.isWithPreviousEvents());
		
		if (request.getAssetIds().size() > MAX_ASSET_REQUEST_SIZE) {
			throw new ServiceException("Asset request size cannot exceed " + MAX_ASSET_REQUEST_SIZE);
		}
		
		try {
			logger.info("GetAssetsRequest: Tenant [" + request.getTenantId() + "] Asset Count [" + request.getAssetIds().size() + "]");
			
			AssetListResponse response = new AssetListResponse();

			List<Asset> assets = assetLoader.setIds(request.getAssetIds()).load();
			for(Asset asset: assets) {
				response.getAssets().add(converter.toServiceDTO(asset));
			}
			
			return response;
		} catch (Exception e) {
			logger.error("Failed Downloading Asset list", e );
			throw new ServiceException(e);			
		}
	}

}
