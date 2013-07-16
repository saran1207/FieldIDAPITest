package com.n4systems.fieldid.service.asset;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.model.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import rfid.ejb.entity.InfoFieldBean;

import java.util.List;
import java.util.concurrent.Callable;

public class AssetTypeService extends FieldIdPersistenceService {

    private static final Logger logger= Logger.getLogger(AssetTypeService.class);

    private @Autowired AsyncService asyncService;
    private @Autowired RecurringScheduleService recurringScheduleService;

    public List<AssetType> getAssetTypes(Long assetTypeGroupId, String name) {
        QueryBuilder<AssetType> queryBuilder = createUserSecurityBuilder(AssetType.class);

        if(assetTypeGroupId != null) {
            if (assetTypeGroupId == -1)
                queryBuilder.addWhere(WhereClauseFactory.createIsNull("group.id"));
            else {
                queryBuilder.addSimpleWhere("group.id", assetTypeGroupId);
            }
        }

        if (name != null) {
            queryBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "name","name", name, WhereParameter.WILDCARD_BOTH, null));
        }

        queryBuilder.addOrder("name");
        return persistenceService.findAll(queryBuilder);
    }

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
        persistenceService.save(recurringEvent.getRecurrence());
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
        for (LocalDateTime dateTime : recurringScheduleService.getBoundedScheduledTimesIterator(recurringEvent.getRecurrence())) {
            recurringScheduleService.scheduleAnEventFor(recurringEvent, dateTime);
        }
    }

    private void removeScheduledEvents(RecurringAssetTypeEvent recurringEvent) {
        QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));

        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);
        builder.addSimpleWhere("recurringEvent", recurringEvent);

        List<Event> events = persistenceService.findAll(builder);
        for (Event event:events) {
            logger.debug("removing scheduled event for asset " + event.getAsset().getIdentifier() + " on " + event.getDueDate());
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

    public List<AssetType> getLotoDevices() {
        QueryBuilder<AssetType> query = createTenantSecurityBuilder(AssetType.class);
        query.addSimpleWhere("group.lotoDevice", true);
        return persistenceService.findAll(query);
    }

    public List<AssetType> getLotoLocks() {
        QueryBuilder<AssetType> query = createTenantSecurityBuilder(AssetType.class);
        query.addSimpleWhere("group.lotoLock", true);
        return persistenceService.findAll(query);
    }

    @Cacheable("assetTypes")
    public List<String> getInfoFieldBeans(Tenant tenant) {
        QueryBuilder<String> query = new QueryBuilder(InfoFieldBean.class, new TenantOnlySecurityFilter(tenant.getId()));

        NewObjectSelect nameSelect = new NewObjectSelect(String.class);
        nameSelect.setConstructorArgs(Lists.newArrayList("LOWER(name)"));
        nameSelect.setDistinct(true);
        query.setSelectArgument(nameSelect);

        return persistenceService.findAll(query);
    }

}