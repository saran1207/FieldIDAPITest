package com.n4systems.model.inspectionschedule;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionScheduleByMobileGuidLoader extends SecurityFilteredLoader<InspectionSchedule> {

	private String mobileGuid;
	

	public InspectionScheduleByMobileGuidLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected InspectionSchedule load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, filter);
		query.addSimpleWhere("mobileGUID", mobileGuid);
		
		return query.getSingleResult(em);
	}


	public InspectionScheduleByMobileGuidLoader setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
		return this;
		
	}
	
	
}
