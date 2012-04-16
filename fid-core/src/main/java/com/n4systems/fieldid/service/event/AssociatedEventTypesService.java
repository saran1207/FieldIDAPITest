package com.n4systems.fieldid.service.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.remover.EventFrequenciesRemovalService;
import com.n4systems.fieldid.service.remover.ScheduleListRemovalService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.util.persistence.QueryBuilder;

public class AssociatedEventTypesService extends FieldIdPersistenceService {

    @Autowired
    private EventFrequenciesRemovalService eventFrequenciesRemovalService;

    @Autowired
    private ScheduleListRemovalService scheduleListRemovalService;

    @Transactional
    public List<AssociatedEventType> getAssociatedEventTypes(AssetType assetType, EventType eventType) {
		QueryBuilder<AssociatedEventType> query = createTenantSecurityBuilder(AssociatedEventType.class);
		if (assetType != null) {
			query.addSimpleWhere("assetType", assetType);
		}

		if (eventType != null) {
			query.addSimpleWhere("eventType", eventType);
		}

		query.addOrder("eventType.name");
		return persistenceService.findAll(query);
    }

    @Transactional
    public void addAndRemove(AssetType assetType, List<EventType> selectedEventTypes) {
        final List<AssociatedEventType> types = getAssociatedEventTypes(assetType, null);

        List<AssociatedEventType> toBeAdded = findEventTypesToAdd(assetType, selectedEventTypes, types);
        List<AssociatedEventType> toBeRemoved = findEventTypesToRemove(selectedEventTypes, types);

        for (AssociatedEventType associatedEventType : toBeRemoved) {
            eventFrequenciesRemovalService.remove(associatedEventType);
            scheduleListRemovalService.remove(associatedEventType.getAssetType(), associatedEventType.getEventType(), EventSchedule.ScheduleStatusGrouping.NON_COMPLETE);
            persistenceService.remove(associatedEventType);
        }

        for (AssociatedEventType associatedEventType : toBeAdded) {
            persistenceService.save(associatedEventType);
        }
        
        // Mobile gets associated event types along with the asset type.  Need to update the mod date so it knows something changed.
        // WEB-2804 We also need to refresh the asset type due to changes made to its associated AssetTypeSchedules in the calls above
        assetType = persistenceService.find(AssetType.class, assetType.getId());
        assetType.touch();
        persistenceService.update(assetType);
    }

    @Transactional
    public void addAndRemove(EventType eventType, List<AssetType> selectedAssetTypes) {
        final List<AssociatedEventType> types = getAssociatedEventTypes(null, eventType);

        List<AssociatedEventType> toBeAdded = findAssetTypesToAdd(eventType, selectedAssetTypes, types);
        List<AssociatedEventType> toBeRemoved = findAssetTypesToRemove(selectedAssetTypes, types);

        for (AssociatedEventType associatedEventType : toBeRemoved) {
            eventFrequenciesRemovalService.remove(associatedEventType);
            scheduleListRemovalService.remove(associatedEventType.getAssetType(), associatedEventType.getEventType(), EventSchedule.ScheduleStatusGrouping.NON_COMPLETE);
            persistenceService.remove(associatedEventType);
 
            associatedEventType.getAssetType().touch();
            persistenceService.update(associatedEventType.getAssetType());
        }

        for (AssociatedEventType associatedEventType : toBeAdded) {
            persistenceService.save(associatedEventType);
            associatedEventType.getAssetType().touch();
            persistenceService.update(associatedEventType.getAssetType());
        }
    }


	private List<AssociatedEventType> findEventTypesToRemove(List<EventType> selectedEventTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeRemoved = new ArrayList<AssociatedEventType>();
		for (AssociatedEventType associatedEventType : types) {
			boolean found = false;

			for (EventType selectedEventType : selectedEventTypes) {
				if (associatedEventType.getEventType().equals(selectedEventType)) {
					found = true;
				}
			}
			if (!found) {
				toBeRemoved.add(associatedEventType);
			}
		}
		return toBeRemoved;
	}

	private List<AssociatedEventType> findEventTypesToAdd(AssetType assetType, List<EventType> selectedEventTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeAdded = new ArrayList<AssociatedEventType>();
		for (EventType selectedEventType : selectedEventTypes) {
			boolean found = false;
			for (AssociatedEventType associatedEventType : types) {
				if (associatedEventType.getEventType().equals(selectedEventType)) {
					found = true;
				}
			}
			if (!found) {
				toBeAdded.add(new AssociatedEventType(selectedEventType, assetType));
			}
		}
		return toBeAdded;
	}

	private List<AssociatedEventType> findAssetTypesToAdd(EventType eventType, List<AssetType> selectedAssetTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeAdded = new ArrayList<AssociatedEventType>();
		for (AssetType selectedAssetType : selectedAssetTypes) {
			boolean found = false;
			for (AssociatedEventType associatedEventType : types) {
				if (associatedEventType.getAssetType().equals(selectedAssetType)) {
					found = true;
				}
			}
			if (!found) {
				toBeAdded.add(new AssociatedEventType( eventType,selectedAssetType));
			}
		}
		return toBeAdded;
	}

	private List<AssociatedEventType> findAssetTypesToRemove(List<AssetType> selectedAssetTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeRemoved = new ArrayList<AssociatedEventType>();
		for (AssociatedEventType associatedEventType : types) {
			boolean found = false;

			for (AssetType selectedAssetType : selectedAssetTypes) {
				if (associatedEventType.getAssetType().equals(selectedAssetType)) {
					found = true;
				}
			}
			if (!found) {
				toBeRemoved.add(associatedEventType);
			}
		}
		return toBeRemoved;
	}

}
