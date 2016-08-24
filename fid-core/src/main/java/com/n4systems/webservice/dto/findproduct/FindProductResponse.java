package com.n4systems.webservice.dto.findproduct;

import com.n4systems.webservice.dto.ProductServiceDTO;

import java.util.ArrayList;
import java.util.List;

public class FindProductResponse {
	
	private List<ProductServiceDTO> products = new ArrayList<ProductServiceDTO>();

	public List<ProductServiceDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductServiceDTO> products) {
		this.products = products;
	}
}
