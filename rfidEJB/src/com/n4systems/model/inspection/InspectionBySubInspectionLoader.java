package com.n4systems.model.inspection;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class InspectionBySubInspectionLoader extends Loader<Inspection> {

	private SubInspection subInspection;
	
	public InspectionBySubInspectionLoader() {}
	
	@Override
	protected Inspection load(EntityManager em) {
		QueryBuilder<Inspection> builder = new QueryBuilder<Inspection>(SubInspectionRelation.class, new OpenSecurityFilter());
		builder.setSimpleSelect("masterInspection");
		builder.addWhere(WhereClauseFactory.create("subInspection.id", subInspection.getId()));
		
		Inspection inspection = builder.getSingleResult(em);
		return inspection;
	}

	public InspectionBySubInspectionLoader setSubInspection(SubInspection subInspection) {
		this.subInspection = subInspection;
		return this;
	}
	
}
