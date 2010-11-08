package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.asset.AssetSubAssetsLoader;
import com.n4systems.model.asset.SmartSearchLoader;

public class RealTimeAssetLookupHandler {

	private final SmartSearchLoader smartSearchLoader; 
	private final AssetSubAssetsLoader subAssetLoader;
	private String searchText;
	private Date modified;
	
	public RealTimeAssetLookupHandler(SmartSearchLoader smartSearchLoader, AssetSubAssetsLoader subAssetLoader) {
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
		
		assets = addAnyNeededSubAssets(assets);
		
		return assets;
	}
	
	public RealTimeAssetLookupHandler setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
	public RealTimeAssetLookupHandler setModified(Date modified) {
		this.modified = modified;
		return this;
	}
	
	private List<Asset> addAnyNeededSubAssets(List<Asset> assets) {
		
		List<Asset> assetsWithSubAssets = subAssetLoader.setAssets(assets).load();
		
		List<Asset> newSubAssets = new ArrayList<Asset>();
		
		for (Asset asset : assetsWithSubAssets) {
			for (SubAsset subAsset : asset.getSubAssets()) {
				newSubAssets.add(subAsset.getAsset());
			}
		}
		
		assets.addAll(newSubAssets);
		
		return assets;
	}
}	
