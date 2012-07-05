package com.n4systems.fieldid.service.asset;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import java.util.List;

public class AssetTypeService extends FieldIdPersistenceService {

    public List<AssetTypeGroup> getAssetTypeGroupsByOrder() {
        QueryBuilder<AssetTypeGroup> queryBuilder = createUserSecurityBuilder(AssetTypeGroup.class);

        queryBuilder.addOrder("orderIdx");

        return persistenceService.findAll(queryBuilder);
    }

    public List<AssetType> getAssetTypes(Long assetTypeGroupId) {
		QueryBuilder<AssetType> builder = createUserSecurityBuilder(AssetType.class);

		if(assetTypeGroupId != null) {
			if (assetTypeGroupId == -1)
				builder.addWhere(WhereClauseFactory.createIsNull("group.id"));
			else {
				builder.addSimpleWhere("group.id", assetTypeGroupId);
			}
		}

		builder.addOrder("name");
		return persistenceService.findAll(builder);
    }

    public void udpateRecurringEvents(AssetType assetType, List<RecurringAssetTypeEvent> recurringEvents) {
        // persist any new ones, delete unused ones, update changed ones before saving assetType itself.
        RecurringAssetTypeEvent toDelete = null;
        List<RecurringAssetTypeEvent> toBeSaved = Lists.newArrayList();
        for (RecurringAssetTypeEvent recurringEvent:recurringEvents) {
            if (isNewRecurringAssetTypeEvent(recurringEvent, assetType)) {
                persistenceService.save(recurringEvent);
            } else if ((toDelete = requiresDelete(recurringEvent, assetType)) != null) {
                persistenceService.delete(toDelete);
                // deal with deleting old schedules here...?
            } else {
                persistenceService.update(recurringEvent); // is this needed???
                // delete old schedules...?
            }
        }
    }

    private RecurringAssetTypeEvent requiresDelete(RecurringAssetTypeEvent recurringEvent, AssetType assetType) {
        RecurringAssetTypeEvent existing = getExistingRecurringEvent(assetType, recurringEvent.getEventType());
        if (existing != null && Recurrence.NONE.equals(recurringEvent.getRecurrence()) ) {
            return existing;
        }
        return null;
    }

    private boolean isNewRecurringAssetTypeEvent(RecurringAssetTypeEvent recurringEvent, AssetType assetType) {
        return getExistingRecurringEvent(assetType, recurringEvent.getEventType())==null && !Recurrence.NONE.equals(recurringEvent.getRecurrence());
    }

    private RecurringAssetTypeEvent getExistingRecurringEvent(AssetType assetType, EventType eventType) {
        for (RecurringAssetTypeEvent recurringAssetTypeEvent:assetType.getRecurringAssetTypeEvents()) {
            if (recurringAssetTypeEvent.getEventType().equals(eventType)) {
                return recurringAssetTypeEvent;
            }
        }
        return null;
    }


}
