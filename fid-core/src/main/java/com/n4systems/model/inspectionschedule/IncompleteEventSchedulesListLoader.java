package com.n4systems.model.inspectionschedule;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class IncompleteEventSchedulesListLoader extends ListLoader<EventSchedule> {
	
	private Asset asset;
	private EventType eventType;

	public IncompleteEventSchedulesListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<EventSchedule> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventSchedule> query = new QueryBuilder<EventSchedule>(EventSchedule.class, filter);

		query.addSimpleWhere("asset", asset).addSimpleWhere("eventType", eventType);
		query.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addOrder("nextDate");
		
		return query.getResultList(em);
	}

	public IncompleteEventSchedulesListLoader setAsset(Asset asset) {
		this.asset = asset;
		return this;
	}

	public IncompleteEventSchedulesListLoader setInspectionType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

}
