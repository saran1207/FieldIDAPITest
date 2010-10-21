package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class HasLinkedProductsLoader extends SecurityFilteredLoader<Boolean> {

	private Long networkId;
	private Long productId;
	
	public HasLinkedProductsLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("networkId", networkId));
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", productId));

		Boolean hasLinkedProducts = builder.entityExists(em);
		return hasLinkedProducts;
	}

	public HasLinkedProductsLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}

	public HasLinkedProductsLoader setProductId(Long productId) {
		this.productId = productId;
		return this;
	}
}
