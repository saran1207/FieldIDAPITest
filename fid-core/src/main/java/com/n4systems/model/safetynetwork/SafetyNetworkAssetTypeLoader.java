package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class SafetyNetworkAssetTypeLoader extends Loader<AssetType> {
	private Long assetTypeId;
	private Long assetNetworkId;

	@Override
	public AssetType load(EntityManager em) {
		QueryBuilder<AssetType> builder = new QueryBuilder<AssetType>(Asset.class, new OpenSecurityFilter());
		builder.setSimpleSelect("type", true);
		builder.addWhere(WhereClauseFactory.create("type.id", assetTypeId));
		builder.addWhere(WhereClauseFactory.create("networkId", assetNetworkId));

		AssetType type = builder.getSingleResult(em);
		return type;
	}

	public SafetyNetworkAssetTypeLoader setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
		return this;
	}

	public SafetyNetworkAssetTypeLoader setAssetNetworkId(Long assetNetworkId) {
		this.assetNetworkId = assetNetworkId;
		return this;
	}

}
