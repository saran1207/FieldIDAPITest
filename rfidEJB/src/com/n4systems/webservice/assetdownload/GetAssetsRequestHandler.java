package com.n4systems.webservice.assetdownload;

import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.model.Product;
import com.n4systems.persistence.loaders.FilteredInListLoader;
import com.n4systems.webservice.RequestHandler;
import com.n4systems.webservice.exceptions.ServiceException;
import com.n4systems.webservice.product.ProductToServiceConverter;

public class GetAssetsRequestHandler implements RequestHandler<AssetRequest, AssetListResponse>{
	private Logger logger = Logger.getLogger(GetAssetsRequestHandler.class);
	private static final int MAX_ASSET_REQUEST_SIZE = 2000;
	
	private final FilteredInListLoader<Product> productLoader;
	private final ProductToServiceConverter converter;
	
	public GetAssetsRequestHandler(FilteredInListLoader<Product> productLoader, ProductToServiceConverter converter) {
		this.productLoader = productLoader;
		this.converter = converter;
	}
	
	@Override
	public AssetListResponse getResponse(AssetRequest request) throws ServiceException {
		converter.setWithPreviosInspections(request.isWithPreviousEvents());
		
		if (request.getAssetIds().size() > MAX_ASSET_REQUEST_SIZE) {
			throw new ServiceException("Asset request size cannot exceed " + MAX_ASSET_REQUEST_SIZE);
		}
		
		try {
			logger.info("GetAssetsRequest: Tenant [" + request.getTenantId() + "] Asset Count [" + request.getAssetIds().size() + "]");
			
			AssetListResponse response = new AssetListResponse();

			List<Product> products = productLoader.setIds(request.getAssetIds()).load();
			for(Product product: products) {
				response.getAssets().add(converter.toServiceDTO(product));
			}
			
			return response;
		} catch (Exception e) {
			logger.error("Failed Downloading Asset list", e );
			throw new ServiceException(e);			
		}
	}

}
