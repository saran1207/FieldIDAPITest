package com.n4systems.model.orgs.secondaryorg;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class CustomerOrgByOwnerListLoader extends ListLoader<CustomerOrg> {
	
	private Long ownerId;
	
	public CustomerOrgByOwnerListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<CustomerOrg> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<CustomerOrg> builder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, filter);
		builder.addSimpleWhere("parent.id", ownerId);
		
		List<CustomerOrg> customers = builder.getResultList(em);
		return customers;
	}

	public CustomerOrgByOwnerListLoader setOwner(BaseOrg owner) {
		return setOwnerId(owner.getId());
	}
	
	public CustomerOrgByOwnerListLoader setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
		return this;
	}
}
