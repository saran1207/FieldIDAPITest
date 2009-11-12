package com.n4systems.webservice.server.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Product;
import com.n4systems.model.product.SmartSearchLoader;

public class RealTimeProductLookupHandler {

	private final SmartSearchLoader smartSearchLoader; 
	private String searchText;
	private Date modified;
	
	public RealTimeProductLookupHandler(SmartSearchLoader smartSearchLoader) {
		this.smartSearchLoader = smartSearchLoader;
	}
	
	public List<Product> lookup() {
		List<Product> products = smartSearchLoader.setSearchText(searchText).load(); 
		
		if (products.size() == 1 && modified != null) {
			if (!products.get(0).getModified().after(modified)) {
				products = new ArrayList<Product>();
			}
		}
		
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
}	
