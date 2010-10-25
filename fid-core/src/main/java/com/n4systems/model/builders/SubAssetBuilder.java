package com.n4systems.model.builders;

import static com.n4systems.model.builders.AssetBuilder.*;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;

public class SubAssetBuilder extends BaseBuilder<SubAsset> {

	private Asset asset;
	private Asset masterAsset;
	
	public static SubAssetBuilder aSubAsset() {
		return new SubAssetBuilder(anAsset().build(), anAsset().build());
	}
	
	public SubAssetBuilder(Asset product, Asset masterAsset) {
		this.asset = product;
		this.masterAsset = masterAsset;
	}
	
	public SubAssetBuilder withMasterProduct(Asset masterAsset) {
		return new SubAssetBuilder(asset, masterAsset);
	}
	
	public SubAssetBuilder containingProduct(Asset product) {
		return new SubAssetBuilder(product, masterAsset);
	}
	
	@Override
	public SubAsset createObject() {
		SubAsset subAsset = new SubAsset();
		subAsset.setId(id);
		subAsset.setAsset(asset);
		subAsset.setMasterAsset(masterAsset);
		
		return subAsset;
	}

}
