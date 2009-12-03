package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.product.ProductSubProductsLoader;
import com.n4systems.model.product.SmartSearchLoader;

public class RealTimeProductLookupHandler {

	private final SmartSearchLoader smartSearchLoader; 
	private final ProductSubProductsLoader subProductLoader;
	private String searchText;
	private Date modified;
	
	public RealTimeProductLookupHandler(SmartSearchLoader smartSearchLoader, ProductSubProductsLoader subProductLoader) {
		this.smartSearchLoader = smartSearchLoader;
		this.subProductLoader = subProductLoader;
	}
	
	public List<Product> lookup() {
		List<Product> products = smartSearchLoader.setSearchText(searchText).load(); 
		
		if (products.size() == 1 && modified != null) {
			if (!products.get(0).getModified().after(modified)) {
				products = new ArrayList<Product>();
			}
		}
		
		products = addAnyNeededSubProducts(products);
		
		return products;
	}
	
	public RealTimeProductLookupHandler setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
	public RealTimeProductLookupHandler setModified(Date modified) {
		this.modified = modified;
		return this;
	}
	
	private List<Product> addAnyNeededSubProducts(List<Product> products) {
		
		List<Product> productsWithSubProducts = subProductLoader.setProducts(products).load(); 
		
		List<Product> newSubProducts = new ArrayList<Product>();
		
		for (Product product : productsWithSubProducts) {
			for (SubProduct subProduct : product.getSubProducts()) {
				newSubProducts.add(subProduct.getProduct());
			}
		}
		
		products.addAll(newSubProducts);
		
		return products;
	}
}	
