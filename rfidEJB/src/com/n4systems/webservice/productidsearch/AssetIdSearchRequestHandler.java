package com.n4systems.webservice.productidsearch;

import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.model.product.ProductIdSearchListLoader;
import com.n4systems.webservice.RequestHandler;
import com.n4systems.webservice.dto.AssetIdListResponse;
import com.n4systems.webservice.dto.AssetSearchRequest;
import com.n4systems.webservice.exceptions.ServiceException;

public class AssetIdSearchRequestHandler implements RequestHandler<AssetSearchRequest, AssetIdListResponse> {
	private final Logger logger = Logger.getLogger(AssetIdSearchRequestHandler.class);
	private final ProductIdSearchListLoader assetIdLoader;
	
	public AssetIdSearchRequestHandler(ProductIdSearchListLoader productIdLoader) {
		this.assetIdLoader = productIdLoader;
	}
	
	@Override
	public AssetIdListResponse getResponse(AssetSearchRequest request) throws ServiceException {
		AssetIdListResponse response = new AssetIdListResponse();
		
		try {
			setupProductIdLoader(request);
			
			List<Long> assetIds = assetIdLoader.load();
			response.setAssetIds(assetIds);
			
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
