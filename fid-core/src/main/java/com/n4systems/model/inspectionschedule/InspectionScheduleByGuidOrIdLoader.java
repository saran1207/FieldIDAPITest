package com.n4systems.model.inspectionschedule;

import javax.persistence.EntityManager;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionScheduleByGuidOrIdLoader extends SecurityFilteredLoader<EventSchedule> {

	private String mobileGuid;
	private long id;
	

	public InspectionScheduleByGuidOrIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected EventSchedule load(EntityManager em, SecurityFilter filter) {
		EventSchedule schedule = null;
		
		if (mobileGuid != null && mobileGuid.trim().length() > 0) {
			schedule = loadByGuid(em, filter);
		}
		
		if (schedule == null) {
			schedule = findByIdUsingEntityManager(em);
		}
		
		return schedule;
	}

	private EventSchedule loadByGuid(EntityManager em,
			SecurityFilter filter) {
		QueryBuilder<EventSchedule> query = getQueryBuilder(filter);
		query.addSimpleWhere("mobileGUID", mobileGuid);
		
		return query.getSingleResult(em);
	}
	

	public InspectionScheduleByGuidOrIdLoader setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
		return this;
	}
	
	public InspectionScheduleByGuidOrIdLoader setId(long id) {
		this.id = id;
		return this;
	}
	
	protected QueryBuilder<EventSchedule> getQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<EventSchedule>(EventSchedule.class, filter);
	}
	
	protected EventSchedule findByIdUsingEntityManager(EntityManager em) {
		return em.find(EventSchedule.class, id);
	}
	
}
