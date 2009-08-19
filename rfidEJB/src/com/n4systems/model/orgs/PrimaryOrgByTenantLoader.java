package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class PrimaryOrgByTenantLoader extends Loader<PrimaryOrg>  {
	private Long tenantId;
	
	public PrimaryOrgByTenantLoader() {
		super();
	}

	@Override
	protected PrimaryOrg load(EntityManager em) {
		QueryBuilder<PrimaryOrg> builder = new QueryBuilder<PrimaryOrg>(PrimaryOrg.class);
		builder.addSimpleWhere("tenant.id", tenantId);
		
		PrimaryOrg org = builder.getSingleResult(em); 
		return org;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
