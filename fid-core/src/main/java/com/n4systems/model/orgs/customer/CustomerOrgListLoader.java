package com.n4systems.model.orgs.customer;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CustomerOrgListLoader extends ListLoader<CustomerOrg> {
	private boolean withLinkedOrgs = true;
	private String[] postFetchFields = new String[0];
	
	private String nameFilter;
	
	public CustomerOrgListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<CustomerOrg> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<CustomerOrg> builder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, filter);
		
		if (!withLinkedOrgs) {
			builder.addWhere(WhereClauseFactory.createIsNull("linkedOrg"));
		}
		
		if(nameFilter != null && !nameFilter.isEmpty()) {
			builder.addWhere(Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.TRIM | WhereParameter.WILDCARD_BOTH);
		}
		
		builder.addOrder("parent.name", "name", "code");
		builder.addPostFetchPaths(postFetchFields);
		
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
	
	public CustomerOrgListLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	public CustomerOrgListLoader withNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}
}
