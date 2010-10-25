package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetSubAssetsLoader extends ListLoader<Asset> {

	private List<Asset> assets;
	
	public AssetSubAssetsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Asset> load(EntityManager em, SecurityFilter filter) {
		List<Asset> assetsWithSubAssets = new ArrayList<Asset>();
		
		QueryBuilder<SubAsset> subAssetQuery = null;
		
		for (Asset asset : assets) {
			subAssetQuery = new QueryBuilder<SubAsset>(SubAsset.class);
			subAssetQuery.addSimpleWhere("masterAsset", asset);
			asset.setSubAssets(subAssetQuery.getResultList(em));
			assetsWithSubAssets.add(asset);
		}
		
		return assetsWithSubAssets;
	}

	public AssetSubAssetsLoader setAssets(List<Asset> assets) {
		this.assets = assets;
		return this;
	}
}
