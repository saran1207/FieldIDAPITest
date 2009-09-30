package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.security.ProductNetworkFilter;
import com.n4systems.persistence.loaders.NonSecuredListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class ChildProductLoader extends NonSecuredListLoader<Product> {

	private Product product;
	
	public ChildProductLoader() {}
	
	@Override
	protected List<Product> load(EntityManager em) {
		if (product == null) {
			throw new SecurityException("product must be set");
		}
		
		List<Product> allLinkedChildProducts = new ArrayList<Product>();
		
		loadChildTree(em, product, allLinkedChildProducts);
				
		return allLinkedChildProducts;
	}
	
	protected void loadChildTree(EntityManager em, Product product, List<Product> allLinkedChildProducts) {
		List<Product> linkedChildren = loadLinkedChildren(em, product);
		
		allLinkedChildProducts.addAll(linkedChildren);
		
		for (Product linkedChild: linkedChildren) {
			loadChildTree(em, linkedChild, allLinkedChildProducts);
		}
	}
	
	protected List<Product> loadLinkedChildren(EntityManager em, Product product) {
		QueryBuilder<Product> loader = new QueryBuilder<Product>(Product.class, new ProductNetworkFilter(product));
		loader.addWhere(WhereClauseFactory.create("linkedProduct", product));

		List<Product> linkedChildren = loader.getResultList(em);
		return linkedChildren;
	}

	public ChildProductLoader setProduct(Product product) {
		this.product = product;
		return this;
	}
	
}
