package com.n4systems.model.builders;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;

import static com.n4systems.model.builders.AssetBuilder.anAsset;

public class SubAssetBuilder extends BaseBuilder<SubAsset> {

	private Asset asset;
	private Asset masterAsset;
	
	public static SubAssetBuilder aSubAsset() {
		return new SubAssetBuilder(anAsset().build(), anAsset().build());
	}
	
	public SubAssetBuilder(Asset asset, Asset masterAsset) {
		this.asset = asset;
		this.masterAsset = masterAsset;
	}
	
	public SubAssetBuilder withMasterAsset(Asset masterAsset) {
		return makeBuilder(new SubAssetBuilder(asset, masterAsset));
	}
	
	public SubAssetBuilder containingAsset(Asset asset) {
		return makeBuilder(new SubAssetBuilder(asset, masterAsset));
	}
	
	@Override
	public SubAsset createObject() {
		SubAsset subAsset = new SubAsset();
		subAsset.setId(getId());
		subAsset.setAsset(asset);
		subAsset.setMasterAsset(masterAsset);
		
		return subAsset;
	}

}
