package com.n4systems.model.utils;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FindSubProducts {
	private final PersistenceManager persistenceManager;
	private final Product product;
	
	public FindSubProducts(PersistenceManager persistenceManager, Product product) {
		super();
		this.persistenceManager = persistenceManager;
		this.product = product;
	}
	
	
	public Product fillInSubProducts() {
		if (product != null) {
			product.setSubProducts(findSubProducts());
		}
		return product;
	}
	
	public List<SubProduct> findSubProducts() {
		QueryBuilder<SubProduct> subProductQuery = new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("masterProduct", product).addOrder("weight").addOrder("created").addOrder("id");
		List<SubProduct> subProducts = persistenceManager.findAll(subProductQuery);
		return subProducts;
	}
	
	
	
}
