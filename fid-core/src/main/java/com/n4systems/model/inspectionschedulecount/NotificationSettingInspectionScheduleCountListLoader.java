package com.n4systems.model.inspectionschedulecount;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;

public abstract class NotificationSettingInspectionScheduleCountListLoader extends ListLoader<InspectionScheduleCount> {

	private NotificationSetting notification;

	public NotificationSettingInspectionScheduleCountListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	
	
	@Override
	protected List<InspectionScheduleCount> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<InspectionScheduleCount> builder = getQueryBuilder(filter);

		prepareSelect(builder);
		applyNotificationTypeFilters(builder);
		applyNotificationFilters(builder);
		applyGroupings(builder);
		applyOrdering(builder);
		
		return builder.getResultList(em);
	}

	protected abstract void applyNotificationTypeFilters(QueryBuilder<InspectionScheduleCount> builder);

	protected void applyOrdering(QueryBuilder<InspectionScheduleCount> builder) {
		builder.addOrder("nextDate");
	}

	protected void applyGroupings(QueryBuilder<InspectionScheduleCount> builder) {
		// the aggregate queries are grouped: next_inspection_date, (customer, division) or (jobsite),  product_type, inspection_type
		builder.addGroupBy("nextDate");
		builder.addGroupBy("owner");
		builder.addGroupBy("product.type.name", "inspectionType.name");
	}

	protected void applyNotificationFilters(QueryBuilder<InspectionScheduleCount> builder) {
		/*
		 * NOTE: only a single producttype and inspectiontype are allowed via the interface.  If we have one
		 * we will use it directly (rather then an in-list)
		 */
		if (!notification.getProductTypes().isEmpty()) {
			builder.addSimpleWhere("product.type.id", notification.getProductTypes().get(0));
		}
		
		if (!notification.getInspectionTypes().isEmpty()) {
			builder.addSimpleWhere("inspectionType.id", notification.getInspectionTypes().get(0));
		}
		
		builder.applyFilter(new OwnerAndDownFilter(notification.getOwner()));
	}

	protected void prepareSelect(QueryBuilder<InspectionScheduleCount> builder) {
		// we have to set the alias here and prefix our select clause arguments, otherwise hibernate generates a bad query
		builder.setTableAlias("isc");
		builder.setSelectArgument(new NewObjectSelect(InspectionScheduleCount.class, "isc.nextDate", "isc.owner", "isc.product.type.name", "isc.inspectionType.name", "count(*)"));
	}

	protected QueryBuilder<InspectionScheduleCount> getQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<InspectionScheduleCount>(InspectionSchedule.class, filter);
	}

	public void setNotificationSetting(NotificationSetting notification) {
		this.notification = notification;
	}

}