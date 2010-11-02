package com.n4systems.model.inspectionschedule;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class IncompleteInspectionSchedulesListLoader extends ListLoader<EventSchedule> {
	
	private Asset asset;
	private InspectionType inspectionType;

	public IncompleteInspectionSchedulesListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<EventSchedule> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventSchedule> query = new QueryBuilder<EventSchedule>(EventSchedule.class, filter);

		query.addSimpleWhere("asset", asset).addSimpleWhere("inspectionType", inspectionType);
		query.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addOrder("nextDate");
		
		return query.getResultList(em);
	}

	public IncompleteInspectionSchedulesListLoader setAsset(Asset asset) {
		this.asset = asset;
		return this;
	}

	public IncompleteInspectionSchedulesListLoader setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

}
