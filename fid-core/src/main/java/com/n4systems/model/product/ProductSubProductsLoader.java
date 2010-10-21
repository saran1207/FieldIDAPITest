package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.SubProduct;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductSubProductsLoader extends ListLoader<Asset> {

	private List<Asset> products;
	
	public ProductSubProductsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Asset> load(EntityManager em, SecurityFilter filter) {
		List<Asset> productsWithSubProducts = new ArrayList<Asset>();
		
		QueryBuilder<SubProduct> subProductQuery = null;
		
		for (Asset asset : products) {
			subProductQuery = new QueryBuilder<SubProduct>(SubProduct.class);
			subProductQuery.addSimpleWhere("masterAsset", asset);
			asset.setSubProducts(subProductQuery.getResultList(em));
			productsWithSubProducts.add(asset);
		}
		
		return productsWithSubProducts;
	}

	public ProductSubProductsLoader setProducts(List<Asset> assets) {
		this.products = assets;
		return this;
	}
}
