package com.n4systems.model.inspectionschedule;

import java.util.Date;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.MinSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class NextEventDateByEventLoader extends SecurityFilteredLoader<Date> {
	private Event event;
	
	public NextEventDateByEventLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Date load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Date> builder = new QueryBuilder<Date>(EventSchedule.class, filter);
		builder.setSelectArgument(new MinSelect("nextDate"));
		builder.addWhere(WhereClauseFactory.create("asset.id", event.getAsset().getId()));
		builder.addWhere(WhereClauseFactory.create("eventType.id", event.getType().getId()));
		builder.addWhere(WhereClauseFactory.create(Comparator.NE, "status", ScheduleStatus.COMPLETED));

		Date nextDate = builder.getSingleResult(em);
		return nextDate;
	}

	public NextEventDateByEventLoader setInspection(Event event) {
		this.event = event;
		return this;
	}
}
