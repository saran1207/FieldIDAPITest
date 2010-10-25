package com.n4systems.model.utils;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FindSubAssets {
	private final PersistenceManager persistenceManager;
	private final Asset asset;
	
	public FindSubAssets(PersistenceManager persistenceManager, Asset product) {
		this.persistenceManager = persistenceManager;
		this.asset = product;
	}
	
	public Asset fillInSubAssets() {
		if (asset != null) {
			asset.setSubAssets(findSubAssets());
		}
		return asset;
	}
	
	public List<SubAsset> findSubAssets() {
		QueryBuilder<SubAsset> subAssetQuery = new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("masterAsset", asset).addOrder("weight").addOrder("created").addOrder("id");
		List<SubAsset> subAssets = persistenceManager.findAll(subAssetQuery);
		return subAssets;
	}
	
}
