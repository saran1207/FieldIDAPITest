package com.n4systems.handlers;

import com.n4systems.model.*;
import com.n4systems.model.eventtype.CommonAssetTypeLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoaderBackedCommonEventTypeHandler implements CommonEventTypeHandler {

	private final CommonAssetTypeLoader assetTypeIdLoader;

	public LoaderBackedCommonEventTypeHandler(CommonAssetTypeLoader assetTypeIdLoader) {
		this.assetTypeIdLoader = assetTypeIdLoader;
	}

	public Set<ThingEventType> findCommonEventTypesFor(List<Long> assetIds) {
		List<AssetType> resultSet = getAssetTypes(assetIds);

		Set<ThingEventType> filterCommonEventTypes = filterCommonEventTypes(resultSet);

		//We will be removing the event types that have ObservationCountCriteria in the form for multi event
		//This is only until we move the page from STRUTS to WICKET
		Set<ThingEventType> removeList = new HashSet<>();

		for(ThingEventType type:filterCommonEventTypes) {
			if(type.getEventForm().getObservationCountGroup() != null) {
				for(CriteriaSection section:type.getEventForm().getSections()) {
					for(Criteria crit: section.getCriteria()) {
						if(crit instanceof ObservationCountCriteria) {
							removeList.add(type);
							break;
						}
					}
				}
			}
		}

		for(ThingEventType type:removeList) {
			filterCommonEventTypes.remove(type);
		}

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
