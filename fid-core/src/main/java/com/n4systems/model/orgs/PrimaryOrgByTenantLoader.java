package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class PrimaryOrgByTenantLoader extends Loader<PrimaryOrg>  {
	private Long tenantId;
	
	@Override
	protected PrimaryOrg load(EntityManager em) {
		QueryBuilder<PrimaryOrg> builder = new QueryBuilder<PrimaryOrg>(PrimaryOrg.class, new OpenSecurityFilter());
		builder.addSimpleWhere("tenant.id", tenantId);
		
		PrimaryOrg org = builder.getSingleResult(em); 
		return org;
	}

	public PrimaryOrgByTenantLoader setTenantId(Long tenantId) {
		this.tenantId = tenantId;
        return this;
	}
}
