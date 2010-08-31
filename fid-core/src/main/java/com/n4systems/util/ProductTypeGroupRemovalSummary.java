package com.n4systems.util;

import com.n4systems.model.ProductTypeGroup;

public class ProductTypeGroupRemovalSummary {
	private ProductTypeGroup group;

	private Long productTypesConnected = 0L;

	public ProductTypeGroupRemovalSummary(ProductTypeGroup group) {
		super();
		this.group = group;
	}

	public ProductTypeGroup getGroup() {
		return group;
	}

	public Long getProductTypesConnected() {
		return productTypesConnected;
	}

	public void setProductTypesConnected(Long productTypesConnected) {
		this.productTypesConnected = productTypesConnected;
	}

}
