package com.n4systems.fieldid.actions.helpers;

import java.util.List;
import java.util.SortedSet;

import com.n4systems.ejb.ProductManager;

public class ProductManagerBackedCommonProductAttributeFinder implements CommonProductAttributeFinder {
	private final ProductManager productManager;

	public ProductManagerBackedCommonProductAttributeFinder(ProductManager productManager) {
		this.productManager = productManager;
	}

	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> productTypeIds) {
		return productManager.findAllCommonInfoFieldNames(productTypeIds);
	}
	

}