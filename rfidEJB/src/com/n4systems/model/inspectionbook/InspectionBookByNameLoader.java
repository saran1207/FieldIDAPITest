package com.n4systems.model.inspectionbook;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionBook;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionBookByNameLoader extends SecurityFilteredLoader<InspectionBook> {

	public BaseOrg owner;
	public String name;
	
	public InspectionBookByNameLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	public InspectionBook load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<InspectionBook> builder = new QueryBuilder<InspectionBook>(InspectionBook.class, filter);
		builder.addSimpleWhere("owner", owner);
		builder.addSimpleWhere("name", name);
		
		return builder.getSingleResult(em);
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
