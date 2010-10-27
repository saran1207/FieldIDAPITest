package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class HasLinkedAssetsLoader extends SecurityFilteredLoader<Boolean> {

	private Long networkId;
	private Long assetId;
	
	public HasLinkedAssetsLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("networkId", networkId));
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", assetId));

		Boolean hasLinkedAssets = builder.entityExists(em);
		return hasLinkedAssets;
	}

	public HasLinkedAssetsLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}

	public HasLinkedAssetsLoader setAssetId(Long assetId) {
		this.assetId = assetId;
		return this;
	}
}
