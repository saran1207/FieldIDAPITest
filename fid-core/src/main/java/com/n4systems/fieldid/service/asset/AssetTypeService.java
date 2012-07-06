package com.n4systems.fieldid.service.asset;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class AssetTypeService extends FieldIdPersistenceService {

    public static final int RECURRING_EVENT_BUFFER_SIZE = 14;  // in days.

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
        RecurringAssetTypeEvent toUpdate = null;
        List<RecurringAssetTypeEvent> toBeSaved = Lists.newArrayList();
        for (RecurringAssetTypeEvent recurringEvent:recurringEvents) {
            if (isNewRecurringAssetTypeEvent(recurringEvent, assetType)) {
                addRecurringEvent(recurringEvent);
            } else if ((toDelete = isDeleted(recurringEvent, assetType)) != null) {
                deleteRecurringEvent(toDelete);
            } else if ((toUpdate = isModified(recurringEvent, assetType))!=null) {
                updateRecurringEvents(toUpdate);
            }
        }
    }

    private RecurringAssetTypeEvent isModified(RecurringAssetTypeEvent recurringEvent, AssetType assetType) {
        RecurringAssetTypeEvent existing = getExistingRecurringEvent(assetType, recurringEvent.getEventType());
        if ((existing==null || !existing.getRecurrence().equals(recurringEvent.getRecurrence())) && !recurringEvent.getRecurrence().equals(Recurrence.NONE)) {
            return existing;
        }
        return null;
    }

    private void updateRecurringEvents(RecurringAssetTypeEvent recurringEvent) {
        // delete the old occurrences and add the new ones.
        RecurringAssetTypeEvent existing = getExistingRecurringEvent(recurringEvent.getAssetType(), recurringEvent.getEventType());
        removeScheduledEvents(existing);

        existing.setRecurrence(recurringEvent.getRecurrence());
        persistenceService.update(recurringEvent);
        scheduleInitialEvents(recurringEvent);
    }

    private void deleteRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        removeScheduledEvents(recurringEvent);
        persistenceService.delete(getExistingRecurringEvent(recurringEvent.getAssetType(), recurringEvent.getEventType()));
    }

    private void addRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        persistenceService.save(recurringEvent);
        scheduleInitialEvents(recurringEvent);
    }

    private RecurringAssetTypeEvent isDeleted(RecurringAssetTypeEvent recurringEvent, AssetType assetType) {
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

    // TODO DD : this stuff should be refactored into new service when events/schedules are merged.
    // e.g. SchedulingService or possibly in EventService?

    private void scheduleInitialEvents(RecurringAssetTypeEvent recurringEvent) {
        QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));
        builder.addSimpleWhere("type", recurringEvent.getAssetType());
        List<Asset> assets = persistenceService.findAll(builder);
        for (Asset asset:assets) {
            LocalDate day = LocalDate.now();
            System.out.println("scheduling events for asset " + asset.getIdentifier());
            for (int i = 0; i < RECURRING_EVENT_BUFFER_SIZE; i++) {
                for (DateTime dateTime: recurringEvent.getRecurrence().getTimesForDay(day)) {
                    System.out.println("- - - - - scheduling " +  recurringEvent.getEventType().getName() + " on " + dateTime.toDate());
                    persistenceService.save(new EventSchedule(asset, recurringEvent.getEventType(), dateTime.toDate()));
                }
                day = day.plusDays(1);
            }
        }
    }


    private void removeScheduledEvents(RecurringAssetTypeEvent recurringEvent) {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(RECURRING_EVENT_BUFFER_SIZE);
        QueryBuilder<EventSchedule> builder = new QueryBuilder<EventSchedule>(EventSchedule.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));

        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "nextDate", from.toDate(), null, WhereClause.ChainOp.AND));
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LT, "to", "nextDate", to.toDate(), null, WhereClause.ChainOp.AND));
        // TODO DD :   AND EventSchedule.RECURRING_ID = recurringEvent.getId()

        List<EventSchedule> schedules = persistenceService.findAll(builder);
        for (EventSchedule schedule:schedules) {
            persistenceService.delete(schedule);
        }
    }


}
