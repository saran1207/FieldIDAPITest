package com.n4systems.webservice.dto.findproduct;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.webservice.dto.ProductServiceDTO;

public class FindProductResponse {
	
	private List<ProductServiceDTO> products = new ArrayList<ProductServiceDTO>();

	public List<ProductServiceDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductServiceDTO> products) {
		this.products = products;
	}
}
