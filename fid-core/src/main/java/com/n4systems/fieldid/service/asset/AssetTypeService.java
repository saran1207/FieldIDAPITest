package com.n4systems.fieldid.service.asset;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.model.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.taskscheduling.task.RecurringSchedulesTask;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;

public class AssetTypeService extends FieldIdPersistenceService {

    private static final Logger logger= Logger.getLogger(AssetTypeService.class);

    private @Autowired AsyncService asyncService;

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

    // NOTE : typically this call is done synchronously and optionally, scheduleInitialEvents is called after. (typically asynchronously because it can be slow).
    public void addRecurringEvent(AssetType assetType, final RecurringAssetTypeEvent recurringEvent) {
        recurringEvent.setTenant(assetType.getTenant());
        persistenceService.save(recurringEvent);
        // NOTE that two things happens here.  a RecurringAssetTypeEvent is saved AND some events are initially created.
        // the first part is quick but the second part is done asynchronously because it can be slow.
        AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
            @Override public Void call() throws Exception {
                scheduleInitialEvents(recurringEvent);
                return null;
            }
        });
        asyncService.run(task);

    }

    public void purgeRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        removeScheduledEvents(recurringEvent);
        deleteRecurringEvent(recurringEvent);
    }

    /**
     * @see #addRecurringEvent(com.n4systems.model.AssetType, com.n4systems.model.RecurringAssetTypeEvent)
     * @param recurringEvent
     */
    private void scheduleInitialEvents(RecurringAssetTypeEvent recurringEvent) {
        QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));
        builder.addSimpleWhere("type", recurringEvent.getAssetType());
        if (recurringEvent.getOwner()!=null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "owner", "owner", recurringEvent.getOwner(), null, WhereClause.ChainOp.AND));
        }

        List<Asset> assets = persistenceService.findAll(builder);
        LocalDate endDate = LocalDate.now().plusDays(RecurringSchedulesTask.RECURRING_EVENT_BUFFER_SIZE);

        // this code is a candidate for "WEB-3129 : Refactor event creation to common service."
        for (Asset asset:assets) {
            Recurrence recurrence = recurringEvent.getRecurrence();
            for (LocalDateTime nextDate:recurrence.getScheduledTimes(LocalDate.now(), endDate)) {
                Event event = new Event();
                event.setNextDate(nextDate.toDate());
                event.setEventState(Event.EventState.OPEN);
                event.setAsset(asset);
                EventGroup eventGroup = new EventGroup();
                event.setStatus(Status.VOID);
                persistenceService.save(eventGroup);
                event.setGroup(eventGroup);
                event.setOwner(asset.getOwner());
                event.setTenant(asset.getTenant());
                event.setType(recurringEvent.getEventType());
                event.setRecurringEvent(recurringEvent);
                persistenceService.save(event);
                updateAssetToNotifyMobileOfChange(asset);
                logger.debug("saving recurring scheduled event " + event.getAsset().getIdentifier() + " on " + event.getNextDate());
            }
        }
    }

    private void updateAssetToNotifyMobileOfChange(Asset asset) {
        asset.touch();
        persistenceService.update(asset);
    }

    private void removeScheduledEvents(RecurringAssetTypeEvent recurringEvent) {
        QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));

        builder.addSimpleWhere("eventState", Event.EventState.OPEN);
        builder.addSimpleWhere("recurringEvent", recurringEvent);
        if (recurringEvent.getOwner()!=null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "owner", "owner", recurringEvent.getOwner(), null, WhereClause.ChainOp.AND));
        }

        List<Event> events = persistenceService.findAll(builder);
        for (Event event:events) {
            logger.debug("removing scheduled event for asset " + event.getAsset().getIdentifier() + " on " + event.getNextDate());
            persistenceService.delete(event);
        }
    }


    public List<RecurringAssetTypeEvent> getRecurringEvents(AssetType assetType) {
        QueryBuilder<RecurringAssetTypeEvent> query = new QueryBuilder<RecurringAssetTypeEvent>(RecurringAssetTypeEvent.class, new TenantOnlySecurityFilter(assetType.getTenant().getId()));
        query.addSimpleWhere("assetType", assetType);
        return persistenceService.findAll(query);
    }


    public void deleteRecurringEvent(AssetType assetType, EventType eventType) {
        QueryBuilder<RecurringAssetTypeEvent> query = new QueryBuilder<RecurringAssetTypeEvent>(RecurringAssetTypeEvent.class, new OpenSecurityFilter());
        query.addSimpleWhere("assetType", assetType);
        query.addSimpleWhere("eventType", eventType);
        List<RecurringAssetTypeEvent> recurringEvents = persistenceService.findAll(query);
        for (RecurringAssetTypeEvent recurringEvent:recurringEvents) {
            deleteRecurringEvent(recurringEvent);
        }
    }

    private void deleteRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        recurringEvent.archiveEntity();
        persistenceService.update(recurringEvent);
    }
}
