package com.n4systems.model.inspectionschedule;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionScheduleByGuidOrIdLoader extends SecurityFilteredLoader<InspectionSchedule> {

	private String mobileGuid;
	private long id;
	

	public InspectionScheduleByGuidOrIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected InspectionSchedule load(EntityManager em, SecurityFilter filter) {
		InspectionSchedule schedule = null;
		
		if (mobileGuid != null && mobileGuid.trim().length() > 0) {
			schedule = loadByGuid(em, filter);
		}
		
		if (schedule == null) {
			schedule = findByIdUsingEntityManager(em);
		}
		
		return schedule;
	}

	private InspectionSchedule loadByGuid(EntityManager em,
			SecurityFilter filter) {
		QueryBuilder<InspectionSchedule> query = getQueryBuilder(filter);
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
	
	protected QueryBuilder<InspectionSchedule> getQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, filter);
	}
	
	protected InspectionSchedule findByIdUsingEntityManager(EntityManager em) {
		return em.find(InspectionSchedule.class, id);
	}
	
}
