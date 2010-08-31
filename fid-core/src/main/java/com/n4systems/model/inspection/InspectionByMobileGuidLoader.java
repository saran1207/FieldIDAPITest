package com.n4systems.model.inspection;

import javax.persistence.EntityManager;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionByMobileGuidLoader<T extends AbstractInspection> extends SecurityFilteredLoader<T> {

	private String mobileGuid;
	private Class<T> clazz;
	
	public InspectionByMobileGuidLoader(SecurityFilter filter, Class<T> clazz) {
		super(filter);
		this.clazz = clazz;
	}
	
	@Override
	protected T load(EntityManager em, SecurityFilter filter) {
		
		QueryBuilder<T> query = new QueryBuilder<T>(clazz, filter);
		query.addSimpleWhere("mobileGUID", mobileGuid);
		query.addPostFetchPaths("attachments");
		
		return query.getSingleResult(em);
	}

	public InspectionByMobileGuidLoader<T> setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
		return this;
	}
}
