package com.n4systems.fieldid.service.asset;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class AssetTypeService extends FieldIdPersistenceService {

    public static final int RECURRING_EVENT_BUFFER_SIZE_IN_DAYS = 14;

    private static final Logger logger= Logger.getLogger(AssetTypeService.class);

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

    public void addRecurringEvent(AssetType assetType, RecurringAssetTypeEvent recurringEvent) {
        persistenceService.save(recurringEvent);
        assetType.add(recurringEvent);
        scheduleInitialEvents(recurringEvent);
    }

    public void deleteRecurringEvent(AssetType assetType, RecurringAssetTypeEvent recurringEvent) {
        removeScheduledEvents(recurringEvent);
        assetType.getRecurringAssetTypeEvents().remove(recurringEvent);
        persistenceService.update(assetType);
    }

    private void scheduleInitialEvents(RecurringAssetTypeEvent recurringEvent) {
        QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));
        builder.addSimpleWhere("type", recurringEvent.getAssetType());
        if (recurringEvent.getOwner()!=null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "owner", "owner", recurringEvent.getOwner(), null, WhereClause.ChainOp.AND));
        }

        List<Asset> assets = persistenceService.findAll(builder);
        LocalDate endDate = LocalDate.now().plusDays(RECURRING_EVENT_BUFFER_SIZE_IN_DAYS);

        for (Asset asset:assets) {
            Recurrence recurrence = recurringEvent.getRecurrence();
            // TODO DD : Do i really need to save EventGroup???? we don't use this info any more?
            for (DateTime nextDate:recurrence.getScheduledTimes(LocalDate.now(), endDate)) {
                Event event = new Event();
                event.setNextDate(nextDate.toDate());
                event.setEventState(Event.EventState.OPEN);
                event.setAsset(asset);
                EventGroup eventGroup = new EventGroup();
                event.setStatus(Status.NA); // TODO DD : Change this to void when implemented.
                persistenceService.save(eventGroup);
                event.setGroup(eventGroup);
                event.setOwner(asset.getOwner());
                event.setTenant(asset.getTenant());
                event.setType(recurringEvent.getEventType());
                event.setRecurringEvent(recurringEvent);
//                event.setSchedule(null); ??  // TODO DD : what to do here???  what does schedule mean at this point? is this concept for mobile only?
                persistenceService.save(event);
                logger.debug("saving recurring scheduled event " + event.getAsset().getIdentifier() + " on " + event.getNextDate());
            }
        }
    }

    private void removeScheduledEvents(RecurringAssetTypeEvent recurringEvent) {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(RECURRING_EVENT_BUFFER_SIZE_IN_DAYS);
        QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));

        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "nextDate", from.toDate(), null, WhereClause.ChainOp.AND));
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LT, "to", "nextDate", to.toDate(), null, WhereClause.ChainOp.AND));
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

}
