package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class ProductsByNetworkId extends ListLoader<Product> {

	private Long networkId;
	
	public ProductsByNetworkId(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<Product> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Product> builder = new QueryBuilder<Product>(Product.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("networkId", networkId));
		builder.addPostFetchPaths("infoOptions");
		
		List<Product> unsecuredProducts = builder.getResultList(em);
		
		PersistenceManager.setSessionReadOnly(em);
		
		List<Product> enhancedProducts = EntitySecurityEnhancer.enhanceList(unsecuredProducts, filter);
		
		return enhancedProducts;
	}

	public ProductsByNetworkId setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}
}
