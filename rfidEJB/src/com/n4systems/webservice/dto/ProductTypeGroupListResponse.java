package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeGroupListResponse extends AbstractListResponse {

	private List<ProductTypeGroupServiceDTO> productTypeGroups = new ArrayList<ProductTypeGroupServiceDTO>();

	public List<ProductTypeGroupServiceDTO> getProductTypeGroups() {
		return productTypeGroups;
	}

	public void setProductTypeGroups(
			List<ProductTypeGroupServiceDTO> productTypeGroups) {
		this.productTypeGroups = productTypeGroups;
	}
}
