package com.n4systems.util;

import com.n4systems.model.AssetTypeGroup;

public class ProductTypeGroupRemovalSummary {
	private AssetTypeGroup group;

	private Long productTypesConnected = 0L;

	public ProductTypeGroupRemovalSummary(AssetTypeGroup group) {
		super();
		this.group = group;
	}

	public AssetTypeGroup getGroup() {
		return group;
	}

	public Long getProductTypesConnected() {
		return productTypesConnected;
	}

	public void setProductTypesConnected(Long productTypesConnected) {
		this.productTypesConnected = productTypesConnected;
	}

}
