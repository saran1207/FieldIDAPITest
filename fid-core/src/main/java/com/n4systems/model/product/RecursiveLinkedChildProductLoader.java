package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.persistence.loaders.NonSecuredListLoader;

public class RecursiveLinkedChildProductLoader extends NonSecuredListLoader<Product> {
	private final LinkedChildProductLoader linkedProductLoader;
	private Product product;
	
	public RecursiveLinkedChildProductLoader(LinkedChildProductLoader linkedProductLoader) {
		this.linkedProductLoader = linkedProductLoader;
	}
	
	public RecursiveLinkedChildProductLoader() {
		this(new LinkedChildProductLoader());
	}
	
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
		List<Product> linkedChildren = linkedProductLoader.setProduct(product).load(em);
		
		allLinkedChildProducts.addAll(linkedChildren);
		
		for (Product linkedChild: linkedChildren) {
			loadChildTree(em, linkedChild, allLinkedChildProducts);
		}
	}

	public RecursiveLinkedChildProductLoader setProduct(Product product) {
		this.product = product;
		return this;
	}
	
}
