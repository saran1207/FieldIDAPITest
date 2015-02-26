package com.n4systems.handlers;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.model.*;
import com.n4systems.model.eventtype.CommonAssetTypeLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoaderBackedCommonEventTypeHandler implements CommonEventTypeHandler {

	private final CommonAssetTypeLoader assetTypeIdLoader;

    private EventTypeService eventTypeService;

	public LoaderBackedCommonEventTypeHandler(CommonAssetTypeLoader assetTypeIdLoader, EventTypeService eventTypeService) {
		this.assetTypeIdLoader = assetTypeIdLoader;
        this.eventTypeService = eventTypeService;
	}

	public Set<ThingEventType> findCommonEventTypesFor(List<Long> assetIds) {
		List<AssetType> resultSet = getAssetTypes(assetIds);

		Set<ThingEventType> filterCommonEventTypes = filterCommonEventTypes(resultSet);

		//TODO - Once this is fixed and we move over from struts to wicket, remember to uncomment out the test "CommonEventTypeHandlerTest.java"

		//We will be removing the event types that have ObservationCountCriteria in the form for multi event
		//This is only until we move the page from STRUTS to WICKET
		Set<ThingEventType> removeList = new HashSet<>();

		if(!filterCommonEventTypes.isEmpty()) {
			for (ThingEventType type : filterCommonEventTypes) {
                //Added this to fix lazy init on the EventForm sections, this handler should be rewritten to use new services
                EventForm form = eventTypeService.getEventType(type.getId()).getEventForm();
				if (form != null && form.getObservationCountGroup() != null) {
					for (CriteriaSection section : form.getSections()) {
						boolean temp = false;
						for (Criteria crit : section.getCriteria()) {
							if (crit instanceof ObservationCountCriteria) {
								removeList.add(type);
								temp = true;
								break;
							}
						}
						if (temp) {
							break;
						}
					}
				}
			}

			for (ThingEventType type : removeList) {
				filterCommonEventTypes.remove(type);
			}
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
