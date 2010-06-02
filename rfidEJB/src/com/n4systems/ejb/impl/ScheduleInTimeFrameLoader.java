package com.n4systems.ejb.impl;

import java.util.Date;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ScheduleInTimeFrameLoader {
	private static final long INSPECTION_SCHEDULE_DATE_RANGE = 30L;
	private final PersistenceManager persistenceManager;

	public ScheduleInTimeFrameLoader(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	
	public List<InspectionSchedule> getSchedulesInTimeFrame(Product product, InspectionType inspectionType, Date datePerformed) {
		
		
		Date to = DateHelper.addDaysToDate(datePerformed, INSPECTION_SCHEDULE_DATE_RANGE);
		
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("product", product).addSimpleWhere("inspectionType", inspectionType);
		query.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addWhere(Comparator.LE, "to", "nextDate", to);
		query.addOrder("nextDate");
		
		return persistenceManager.findAll(query);
	}
}
