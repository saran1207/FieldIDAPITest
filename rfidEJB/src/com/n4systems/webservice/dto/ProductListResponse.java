package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductListResponse extends AbstractListResponse {

	private List<ProductServiceDTO> products = new ArrayList<ProductServiceDTO>();

	public List<ProductServiceDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductServiceDTO> products) {
		this.products = products;
	}
}
