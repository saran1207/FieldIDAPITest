package com.n4systems.model.inspectiontype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionTypeListLoader extends ListLoader<InspectionType> {

	public InspectionTypeListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<InspectionType> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<InspectionType> builder = new QueryBuilder<InspectionType>(InspectionType.class, filter);
		List<InspectionType> inspectionTypes = builder.getResultList(em);
		return inspectionTypes;
	}

	

}
