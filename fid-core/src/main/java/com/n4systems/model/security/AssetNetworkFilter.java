package com.n4systems.model.security;

import com.n4systems.model.Asset;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetNetworkFilter implements QueryFilter {

	public final Long networkId;
	
	public AssetNetworkFilter(Long networkId) {
		this.networkId = networkId;
	}
	
	public AssetNetworkFilter(Asset asset) {
		this(asset.getNetworkId());
	}
	
	public void applyFilter(QueryBuilder<?> builder) {
		builder.addWhere(WhereClauseFactory.create("networkId", networkId));
	}
	
}
