package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.asset.AssetSubAssetsLoader;
import com.n4systems.model.asset.SmartSearchLoader;

public class RealTimeProductLookupHandler {

	private final SmartSearchLoader smartSearchLoader; 
	private final AssetSubAssetsLoader subAssetLoader;
	private String searchText;
	private Date modified;
	
	public RealTimeProductLookupHandler(SmartSearchLoader smartSearchLoader, AssetSubAssetsLoader subAssetLoader) {
		this.smartSearchLoader = smartSearchLoader;
		this.subAssetLoader = subAssetLoader;
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
		
		List<Asset> productsWithSubProducts = subAssetLoader.setAssets(assets).load();
		
		List<Asset> newSubProducts = new ArrayList<Asset>();
		
		for (Asset asset : productsWithSubProducts) {
			for (SubAsset subAsset : asset.getSubAssets()) {
				newSubProducts.add(subAsset.getAsset());
			}
		}
		
		assets.addAll(newSubProducts);
		
		return assets;
	}
}	
