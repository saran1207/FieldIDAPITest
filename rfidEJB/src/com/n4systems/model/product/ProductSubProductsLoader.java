package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductSubProductsLoader extends ListLoader<Product> {

	private List<Product> products;
	
	public ProductSubProductsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Product> load(EntityManager em, SecurityFilter filter) {
		List<Product> productsWithSubProducts = new ArrayList<Product>();
		
		QueryBuilder<SubProduct> subProductQuery = null;
		
		for (Product product : products) {
			subProductQuery = new QueryBuilder<SubProduct>(SubProduct.class, filter);
			subProductQuery.addSimpleWhere("masterProduct", product);
			product.setSubProducts(subProductQuery.getResultList(em));
			productsWithSubProducts.add(product);
		}
		
		return productsWithSubProducts;
	}

	public ProductSubProductsLoader setProducts(List<Product> products) {
		this.products = products;
		return this;
	}
}
