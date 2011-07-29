package com.n4systems.model.asset;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.AssetNetworkFilter;
import com.n4systems.persistence.loaders.NonSecuredListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class LinkedChildAssetLoader extends NonSecuredListLoader<Asset> {

	protected Asset asset;

	public LinkedChildAssetLoader() {}
	
	@Override
	public List<Asset> load(EntityManager em) {
		if (asset == null) {
			throw new SecurityException("asset must be set");
		}
		
		QueryBuilder<Asset> loader = new QueryBuilder<Asset>(Asset.class, new AssetNetworkFilter(asset));
		loader.addWhere(WhereClauseFactory.create("linkedAsset.id", asset.getId()));

		List<Asset> linkedChildren = loader.getResultList(em);
		return linkedChildren;
	}

	
	public LinkedChildAssetLoader setAsset(Asset asset) {
		this.asset = asset;
		return this;
	}
}
