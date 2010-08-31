package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeListResponse extends AbstractListResponse {
	
	private List<ProductTypeServiceDTO> productTypes = new ArrayList<ProductTypeServiceDTO>();

	public List<ProductTypeServiceDTO> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<ProductTypeServiceDTO> productTypes) {
		this.productTypes = productTypes;
	}

}
