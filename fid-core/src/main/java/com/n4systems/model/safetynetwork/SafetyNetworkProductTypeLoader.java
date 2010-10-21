package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class SafetyNetworkProductTypeLoader extends Loader<AssetType> {
	private Long productTypeId;
	private Long productNetworkid;

	@Override
	protected AssetType load(EntityManager em) {
		QueryBuilder<AssetType> builder = new QueryBuilder<AssetType>(Asset.class, new OpenSecurityFilter());
		builder.setSimpleSelect("type", true);
		builder.addWhere(WhereClauseFactory.create("type.id", productTypeId));
		builder.addWhere(WhereClauseFactory.create("networkId", productNetworkid));

		AssetType type = builder.getSingleResult(em);
		return type;
	}

	public SafetyNetworkProductTypeLoader setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
		return this;
	}

	public SafetyNetworkProductTypeLoader setProductNetworkid(Long productNetworkid) {
		this.productNetworkid = productNetworkid;
		return this;
	}

}
