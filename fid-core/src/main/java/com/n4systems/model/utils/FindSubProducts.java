package com.n4systems.model.utils;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Asset;
import com.n4systems.model.SubProduct;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FindSubProducts {
	private final PersistenceManager persistenceManager;
	private final Asset asset;
	
	public FindSubProducts(PersistenceManager persistenceManager, Asset product) {
		super();
		this.persistenceManager = persistenceManager;
		this.asset = product;
	}
	
	
	public Asset fillInSubProducts() {
		if (asset != null) {
			asset.setSubProducts(findSubProducts());
		}
		return asset;
	}
	
	public List<SubProduct> findSubProducts() {
		QueryBuilder<SubProduct> subProductQuery = new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("masterAsset", asset).addOrder("weight").addOrder("created").addOrder("id");
		List<SubProduct> subProducts = persistenceManager.findAll(subProductQuery);
		return subProducts;
	}
	
	
	
}
