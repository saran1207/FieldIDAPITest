package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.SubProduct;
import com.n4systems.model.product.ProductSubProductsLoader;
import com.n4systems.model.product.SmartSearchLoader;

public class RealTimeProductLookupHandler {

	private final SmartSearchLoader smartSearchLoader; 
	private final ProductSubProductsLoader subProductLoader;
	private String searchText;
	private Date modified;
	
	public RealTimeProductLookupHandler(SmartSearchLoader smartSearchLoader, ProductSubProductsLoader subProductLoader) {
		this.smartSearchLoader = smartSearchLoader;
		this.subProductLoader = subProductLoader;
	}
	
	public List<Asset> lookup() {
		List<Asset> assets = smartSearchLoader.setSearchText(searchText).load();
		
		if (assets.size() == 1 && modified != null) {
			if (!assets.get(0).getModified().after(modified)) {
				assets = new ArrayList<Asset>();
			}
		}
		
		assets = addAnyNeededSubProducts(assets);
		
		return assets;
	}
	
	public RealTimeProductLookupHandler setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
	public RealTimeProductLookupHandler setModified(Date modified) {
		this.modified = modified;
		return this;
	}
	
	private List<Asset> addAnyNeededSubProducts(List<Asset> assets) {
		
		List<Asset> productsWithSubProducts = subProductLoader.setProducts(assets).load();
		
		List<Asset> newSubProducts = new ArrayList<Asset>();
		
		for (Asset asset : productsWithSubProducts) {
			for (SubProduct subProduct : asset.getSubProducts()) {
				newSubProducts.add(subProduct.getAsset());
			}
		}
		
		assets.addAll(newSubProducts);
		
		return assets;
	}
}	
