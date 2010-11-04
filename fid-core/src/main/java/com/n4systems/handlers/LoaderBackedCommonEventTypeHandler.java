package com.n4systems.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.CommonAssetTypeLoader;

public class LoaderBackedCommonEventTypeHandler implements CommonEventTypeHandler {

	private final CommonAssetTypeLoader assetTypeIdLoader;

	public LoaderBackedCommonEventTypeHandler(CommonAssetTypeLoader assetTypeIdLoader) {
		this.assetTypeIdLoader = assetTypeIdLoader;
	}

	public Set<EventType> findCommonEventTypesFor(List<Long> assetIds) {
		List<AssetType> resultSet = getAssetTypes(assetIds);

		Set<EventType> filterCommonEventTypes = filterCommonEventTypes(resultSet);
		return filterCommonEventTypes == null ? new HashSet<EventType>() : filterCommonEventTypes;
	}

	private List<AssetType> getAssetTypes(List<Long> assetIds) {
		if (assetIds.isEmpty()) {
			return new ArrayList<AssetType>();
		}
		
		return assetTypeIdLoader.forAssets(assetIds).load();
	}

	@SuppressWarnings("deprecation")
	private Set<EventType> filterCommonEventTypes(
			List<AssetType> resultSet) {
		Set<EventType> commonEventTypes = null;
		for (AssetType assetType : resultSet) {
			Set<EventType> currentEventTypes = assetType.getEventTypes();
			if (commonEventTypes == null) {
				commonEventTypes = currentEventTypes;
			} else {
				commonEventTypes.retainAll(currentEventTypes);
			}
		}
		return commonEventTypes;
	}

}
