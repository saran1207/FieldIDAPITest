package com.n4systems.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AssetType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.CommonAssetTypeLoader;

public class LoaderBackedCommonInspectionTypeHandler implements CommonInspectionTypeHandler {

	private final CommonAssetTypeLoader assetTypeIdLoader;

	public LoaderBackedCommonInspectionTypeHandler(CommonAssetTypeLoader assetTypeIdLoader) {
		this.assetTypeIdLoader = assetTypeIdLoader;
	}

	public Set<InspectionType> findCommonInspectionTypesFor(List<Long> assetIds) {
	

		List<AssetType> resultSet = getAssetTypes(assetIds);

		Set<InspectionType> filterCommonInspectionTypes = filterCommonInspectionTypes(resultSet);
		return filterCommonInspectionTypes == null ? new HashSet<InspectionType>() : filterCommonInspectionTypes;
	}

	private List<AssetType> getAssetTypes(List<Long> assetIds) {
		if (assetIds.isEmpty()) {
			return new ArrayList<AssetType>();
		}
		
		return assetTypeIdLoader.forAssets(assetIds).load();
	}

	@SuppressWarnings("deprecation")
	private Set<InspectionType> filterCommonInspectionTypes(
			List<AssetType> resultSet) {
		Set<InspectionType> commonInspectionTypes = null;
		for (AssetType assetType : resultSet) {
			Set<InspectionType> currentInspectionTypes = assetType.getInspectionTypes();
			if (commonInspectionTypes == null) {
				commonInspectionTypes = currentInspectionTypes;
			} else {
				commonInspectionTypes.retainAll(currentInspectionTypes);
			}
		}
		return commonInspectionTypes;
	}

}
