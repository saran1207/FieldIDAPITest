package com.n4systems.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.SetUtils;

import com.google.common.collect.Sets;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.inspectiontype.CommonProductTypeLoader;

public class LoaderBackedCommonInspectionTypeHandler implements CommonInspectionTypeHandler {

	private final CommonProductTypeLoader productTypeIdLoader;

	public LoaderBackedCommonInspectionTypeHandler(
			CommonProductTypeLoader productTypeIdLoader) {
		this.productTypeIdLoader = productTypeIdLoader;
	}

	public Set<InspectionType> findCommonInspectionTypesFor(List<Long> assetIds) {
	

		List<ProductType> resultSet = getProductTypes(assetIds);

		Set<InspectionType> filterCommonInspectionTypes = filterCommonInspectionTypes(resultSet);
		return filterCommonInspectionTypes == null ? new HashSet<InspectionType>() : filterCommonInspectionTypes;
	}

	private List<ProductType> getProductTypes(List<Long> assetIds) {
		if (assetIds.isEmpty()) {
			return new ArrayList<ProductType>();
		}
		
		return productTypeIdLoader.forAssets(assetIds).load();
	}

	private Set<InspectionType> filterCommonInspectionTypes(
			List<ProductType> resultSet) {
		Set<InspectionType> commonInspectionTypes = null;
		for (ProductType productType : resultSet) {
			Set<InspectionType> currentInspectionTypes = productType.getInspectionTypes();
			if (commonInspectionTypes == null) {
				commonInspectionTypes = currentInspectionTypes;
			} else {
				commonInspectionTypes.retainAll(currentInspectionTypes);
			}
		}
		return commonInspectionTypes;
	}

}
