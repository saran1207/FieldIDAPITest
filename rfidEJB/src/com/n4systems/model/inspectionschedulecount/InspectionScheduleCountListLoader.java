package com.n4systems.model.inspectionschedulecount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class InspectionScheduleCountListLoader extends ListLoader<InspectionScheduleCount> {
	private Date fromDate;
	private Date toDate;
	private NotificationSetting notification;

	public InspectionScheduleCountListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<InspectionScheduleCount> load(EntityManager em, SecurityFilter filter) {
		
		List<InspectionScheduleCount> scheduleCounts = new ArrayList<InspectionScheduleCount>();
		QueryBuilder<InspectionScheduleCount> builder;
		
		/*
		 * When using customer/divisions, we _could_ use an in list of each.  The problem is that
		 * we may have customers with null divisions which means an in list is impossible.
		 * Our only other choice would be to build a list of (customer = :c1 AND division = :d1) or (customer = c2 AND ....
		 * this could create a very long list of criteria and would produce a massive SQL query.  Rather than do this
		 * we've opted to handle the customers/divisions one at a time.
		 */
		
		builder = prepareQueryBuilder(filter);
		
		scheduleCounts.addAll(builder.getResultList(em));
		
		return scheduleCounts;
  }
	
	private QueryBuilder<InspectionScheduleCount> prepareQueryBuilder(SecurityFilter filter) {
		QueryBuilder<InspectionScheduleCount> builder = new QueryBuilder<InspectionScheduleCount>(InspectionSchedule.class, filter);
		
		// we have to set the alias here and prefix our select clause arguments, otherwise hibernate generates a bad query 
		builder.setTableAlias("isc");
		builder.setSelectArgument(new NewObjectSelect(InspectionScheduleCount.class, "isc.nextDate", "isc.owner.name", "isc.product.type.name", "isc.inspectionType.name", "count(*)"));
		
		// we only want schedules that have not been completed
		builder.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		
		// from dates are inclusive, to dates are exclusive.  See the RelativeTime class for why it works this way
		builder.addWhere(Comparator.GE, "fromDate", "nextDate", fromDate);
		builder.addWhere(Comparator.LT, "toDate", "nextDate", toDate);

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
		
		// the aggregate queries are grouped: next_inspection_date, (customer, division) or (jobsite),  product_type, inspection_type
		builder.addGroupBy("nextDate");
		builder.addGroupBy("owner.name");
		builder.addGroupBy("product.type.name", "inspectionType.name");
		
		// we also want to order, the same way we group
		builder.addOrder("nextDate", true);
		builder.addOrder("owner.name");
		builder.addOrder("product.type.name", "inspectionType.name");
		
		return builder;
	}
	
	public void setFromDate(Date fromDate) {
    	this.fromDate = fromDate;
    }

	public void setToDate(Date toDate) {
    	this.toDate = toDate;
    }

	public void setNotification(NotificationSetting notification) {
    	this.notification = notification;
    }
	
}
