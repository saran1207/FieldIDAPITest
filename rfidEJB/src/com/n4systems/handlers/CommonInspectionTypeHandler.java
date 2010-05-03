package com.n4systems.handlers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.CommonProductTypeLoader;

public class CommonInspectionTypeHandler {

	private final CommonProductTypeLoader productTypeIdLoader;

	public CommonInspectionTypeHandler(CommonProductTypeLoader productTypeIdLoader) {
		this.productTypeIdLoader = productTypeIdLoader;
	}

	
	public List<InspectionType> findCommonInspectionTypesFor(List<Long> assetIds) {
		if (assetIds.isEmpty()) {
			return new ArrayList<InspectionType>();
		}
		
		productTypeIdLoader.forAssets(assetIds).load();
		
		return new ArrayList<InspectionType>();
	}

}
