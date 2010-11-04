package com.n4systems.model.inspectionschedulecount;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;

public abstract class NotificationSettingEventScheduleCountListLoader extends ListLoader<EventScheduleCount> {

	private NotificationSetting notification;

	public NotificationSettingEventScheduleCountListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	
	
	@Override
	protected List<EventScheduleCount> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventScheduleCount> builder = getQueryBuilder(filter);

		prepareSelect(builder);
		applyNotificationTypeFilters(builder);
		applyNotificationFilters(builder);
		applyGroupings(builder);
		applyOrdering(builder);
		
		return builder.getResultList(em);
	}

	protected abstract void applyNotificationTypeFilters(QueryBuilder<EventScheduleCount> builder);

	protected void applyOrdering(QueryBuilder<EventScheduleCount> builder) {
		builder.addOrder("nextDate");
	}

	protected void applyGroupings(QueryBuilder<EventScheduleCount> builder) {
		// the aggregate queries are grouped: next_event_date, (customer, division) or (jobsite),  asset_type, event_type
		builder.addGroupBy("nextDate");
		builder.addGroupBy("owner");
		builder.addGroupBy("asset.type.name", "eventType.name");
	}

	protected void applyNotificationFilters(QueryBuilder<EventScheduleCount> builder) {
		/*
		 * NOTE: only a single assettype and eventtype are allowed via the interface.  If we have one
		 * we will use it directly (rather then an in-list)
		 */
		if (!notification.getAssetTypes().isEmpty()) {
			builder.addSimpleWhere("asset.type.id", notification.getAssetTypes().get(0));
		}
		
		if (!notification.getEventTypes().isEmpty()) {
			builder.addSimpleWhere("eventType.id", notification.getEventTypes().get(0));
		}
		
		builder.applyFilter(new OwnerAndDownFilter(notification.getOwner()));
	}

	protected void prepareSelect(QueryBuilder<EventScheduleCount> builder) {
		// we have to set the alias here and prefix our select clause arguments, otherwise hibernate generates a bad query
		builder.setTableAlias("isc");
		builder.setSelectArgument(new NewObjectSelect(EventScheduleCount.class, "isc.nextDate", "isc.owner", "isc.asset.type.name", "isc.eventType.name", "count(*)"));
	}

	protected QueryBuilder<EventScheduleCount> getQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<EventScheduleCount>(EventSchedule.class, filter);
	}

	public void setNotificationSetting(NotificationSetting notification) {
		this.notification = notification;
	}

}