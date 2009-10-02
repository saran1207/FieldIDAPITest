package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.security.ProductNetworkFilter;
import com.n4systems.persistence.loaders.NonSecuredListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class LinkedChildProductLoader extends NonSecuredListLoader<Product> {

	protected Product product;

	public LinkedChildProductLoader() {}
	
	@Override
	protected List<Product> load(EntityManager em) {
		if (product == null) {
			throw new SecurityException("product must be set");
		}
		
		QueryBuilder<Product> loader = new QueryBuilder<Product>(Product.class, new ProductNetworkFilter(product));
		loader.addWhere(WhereClauseFactory.create("linkedProduct.id", product.getId()));

		List<Product> linkedChildren = loader.getResultList(em);
		return linkedChildren;
	}

	
	public LinkedChildProductLoader setProduct(Product product) {
		this.product = product;
		return this;
	}
}
