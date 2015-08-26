package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.remover.EventFrequenciesRemovalService;
import com.n4systems.fieldid.service.remover.ScheduleListRemovalService;
import com.n4systems.model.*;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class AssociatedEventTypesService extends FieldIdPersistenceService {

    @Autowired
    private EventFrequenciesRemovalService eventFrequenciesRemovalService;

    @Autowired
    private ScheduleListRemovalService scheduleListRemovalService;

    @Autowired
    private AssetTypeService assetTypeService;

	public List<AssociatedEventType> getAssociatedEventTypes(AssetType assetType) {
		return getAssociatedEventTypes(assetType, null);
	}

	public List<AssociatedEventType> getAssociatedEventTypes(EventType eventType) {
		return getAssociatedEventTypes(null, eventType);
	}

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
    public void addAndRemove(AssetType assetType, List<ThingEventType> selectedEventTypes) {
        final List<AssociatedEventType> types = getAssociatedEventTypes(assetType, null);

        List<AssociatedEventType> toBeAdded = findEventTypesToAdd(assetType, selectedEventTypes, types);
        List<AssociatedEventType> toBeRemoved = findEventTypesToRemove(selectedEventTypes, types);

        for (AssociatedEventType associatedEventType : toBeRemoved) {
            eventFrequenciesRemovalService.remove(associatedEventType);
            // NOTE : EventSchedules are now deprecated (july 2012). so we'll just logically delete them to keep data integrity so mobile app won't complain.
            // the real work will be done on the Events table via deleteAssociatedEvents().
            assetTypeService.deleteRecurringEvent(assetType, associatedEventType.getEventType());
            scheduleListRemovalService.deleteAssociatedEvents(associatedEventType.getAssetType(), associatedEventType.getEventType());
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
    public void addAndRemove(ThingEventType eventType, List<AssetType> selectedAssetTypes) {
        final List<AssociatedEventType> types = getAssociatedEventTypes(null, eventType);

        List<AssociatedEventType> toBeAdded = findAssetTypesToAdd(eventType, selectedAssetTypes, types);
        List<AssociatedEventType> toBeRemoved = findAssetTypesToRemove(selectedAssetTypes, types);

        for (AssociatedEventType associatedEventType : toBeRemoved) {
            eventFrequenciesRemovalService.remove(associatedEventType);

            assetTypeService.deleteRecurringEvent(associatedEventType.getAssetType(), associatedEventType.getEventType());
            scheduleListRemovalService.deleteAssociatedEvents(associatedEventType.getAssetType(), associatedEventType.getEventType());

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


	private List<AssociatedEventType> findEventTypesToRemove(List<ThingEventType> selectedEventTypes, List<AssociatedEventType> types) {
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

	private List<AssociatedEventType> findEventTypesToAdd(AssetType assetType, List<ThingEventType> selectedEventTypes, List<AssociatedEventType> types) {
		List<AssociatedEventType> toBeAdded = new ArrayList<AssociatedEventType>();
		for (ThingEventType selectedEventType : selectedEventTypes) {
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

	private List<AssociatedEventType> findAssetTypesToAdd(ThingEventType eventType, List<AssetType> selectedAssetTypes, List<AssociatedEventType> types) {
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
