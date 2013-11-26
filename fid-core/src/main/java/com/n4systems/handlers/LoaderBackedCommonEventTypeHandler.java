package com.n4systems.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.eventtype.CommonAssetTypeLoader;

public class LoaderBackedCommonEventTypeHandler implements CommonEventTypeHandler {

	private final CommonAssetTypeLoader assetTypeIdLoader;

	public LoaderBackedCommonEventTypeHandler(CommonAssetTypeLoader assetTypeIdLoader) {
		this.assetTypeIdLoader = assetTypeIdLoader;
	}

	public Set<ThingEventType> findCommonEventTypesFor(List<Long> assetIds) {
		List<AssetType> resultSet = getAssetTypes(assetIds);

		Set<ThingEventType> filterCommonEventTypes = filterCommonEventTypes(resultSet);
		return filterCommonEventTypes == null ? new HashSet<ThingEventType>() : filterCommonEventTypes;
	}

	private List<AssetType> getAssetTypes(List<Long> assetIds) {
		if (assetIds.isEmpty()) {
			return new ArrayList<AssetType>();
		}
		
		return assetTypeIdLoader.forAssets(assetIds).load();
	}

	@SuppressWarnings("deprecation")
	private Set<ThingEventType> filterCommonEventTypes(
			List<AssetType> resultSet) {
		Set<ThingEventType> commonEventTypes = null;
		for (AssetType assetType : resultSet) {
			Set<ThingEventType> currentEventTypes = assetType.getEventTypes();
			if (commonEventTypes == null) {
				commonEventTypes = currentEventTypes;
			} else {
				commonEventTypes.retainAll(currentEventTypes);
			}
		}
		return commonEventTypes;
	}

}
