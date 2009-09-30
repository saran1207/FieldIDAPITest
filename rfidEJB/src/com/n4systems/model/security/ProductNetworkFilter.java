package com.n4systems.model.security;

import com.n4systems.model.Product;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClauseFactory;

public class ProductNetworkFilter implements QueryFilter {

	public final Long networkId;
	
	public ProductNetworkFilter(Long networkId) {
		this.networkId = networkId;
	}
	
	public ProductNetworkFilter(Product product) {
		this(product.getNetworkId());
	}
	
	public void applyFilter(QueryBuilder<?> builder) {
		builder.addWhere(WhereClauseFactory.create("networkId", networkId));
	}
	
}
