package com.n4systems.model.orgs.customer;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class CustomerOrgListLoader extends ListLoader<CustomerOrg> {
	private boolean withLinkedOrgs = true;
	
	public CustomerOrgListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<CustomerOrg> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<CustomerOrg> builder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, filter);
		
		if (!withLinkedOrgs) {
			builder.addWhere(WhereClauseFactory.createIsNull("linkedOrg"));
		}
		
		builder.addOrder("parent.name", "name", "code");
		
		List<CustomerOrg> customers = builder.getResultList(em);
		return customers;
	}

	public CustomerOrgListLoader withLinkedOrgs() {
		withLinkedOrgs = true;
		return this;
	}
	
	public CustomerOrgListLoader withoutLinkedOrgs() {
		withLinkedOrgs = false;
		return this;
	}
}
