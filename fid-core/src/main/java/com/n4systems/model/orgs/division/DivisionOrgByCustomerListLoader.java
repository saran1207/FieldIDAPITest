package com.n4systems.model.orgs.division;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class DivisionOrgByCustomerListLoader extends ListLoader<DivisionOrg> {
	
	private Long customerId;
	
	public DivisionOrgByCustomerListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<DivisionOrg> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<DivisionOrg> builder = new QueryBuilder<DivisionOrg>(DivisionOrg.class, filter);
		builder.addSimpleWhere("parent.id", customerId);
		
		List<DivisionOrg> divisions = builder.getResultList(em);
		return divisions;
	}

	public DivisionOrgByCustomerListLoader setCustomer(CustomerOrg customer) {
		this.customerId = customer.getId();
		return this;
	}
	
	public DivisionOrgByCustomerListLoader setCustomerId(Long customerId) {
		this.customerId = customerId;
		return this;
	}
}
