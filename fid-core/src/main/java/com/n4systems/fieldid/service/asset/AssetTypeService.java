package com.n4systems.fieldid.service.asset;

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

    public static final int RECURRING_EVENT_BUFFER_SIZE_IN_DAYS = 14;

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
        persistenceService.update(assetType);
        scheduleInitialEvents(recurringEvent);
    }

    public void deleteRecurringEvent(AssetType assetType, RecurringAssetTypeEvent recurringEvent) {
        removeScheduledEvents(recurringEvent);
        assetType.getRecurringAssetTypeEvents().remove(recurringEvent);
        persistenceService.update(assetType);
    }


    // TODO DD : this stuff should be refactored into new service when events/schedules are merged.
    // e.g. SchedulingService or possibly in EventService?

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
            for (DateTime nextDate:recurrence.getScheduledTimes(LocalDate.now(), endDate)) {
                persistenceService.save(new EventSchedule(asset, recurringEvent.getEventType(), nextDate.toDate()));
            }
        }
    }

    private void removeScheduledEvents(RecurringAssetTypeEvent recurringEvent) {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(RECURRING_EVENT_BUFFER_SIZE_IN_DAYS);
        QueryBuilder<EventSchedule> builder = new QueryBuilder<EventSchedule>(EventSchedule.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));

        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "nextDate", from.toDate(), null, WhereClause.ChainOp.AND));
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LT, "to", "nextDate", to.toDate(), null, WhereClause.ChainOp.AND));
        if (recurringEvent.getOwner()!=null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "owner", "owner", recurringEvent.getOwner(), null, WhereClause.ChainOp.AND));
        }
        // TODO DD :   AND EventSchedule.RECURRING_ID = recurringEvent.getId() when schedules is merged!!!

        List<EventSchedule> schedules = persistenceService.findAll(builder);
        for (EventSchedule schedule:schedules) {
            System.out.println("removing scheduled event for asset " + schedule.getAsset().getIdentifier() + " on " + schedule.getNextDate());
            persistenceService.delete(schedule);
        }
    }


}
