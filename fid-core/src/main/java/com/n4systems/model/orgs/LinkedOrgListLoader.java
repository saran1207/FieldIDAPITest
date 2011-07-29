package com.n4systems.model.orgs;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.NonSecuredListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class LinkedOrgListLoader extends NonSecuredListLoader<ExternalOrg> {
	
	private Long internalOrgId;
	
	@Override
	public List<ExternalOrg> load(EntityManager em) {
		QueryBuilder<ExternalOrg> builder = new QueryBuilder<ExternalOrg>(ExternalOrg.class, new OpenSecurityFilter());
		builder.addSimpleWhere("linkedOrg.id", internalOrgId);
		
		List<ExternalOrg> linkedOrgs = builder.getResultList(em);
		return linkedOrgs;
	}

	public LinkedOrgListLoader setInternalOrgId(Long internalOrgId) {
		this.internalOrgId = internalOrgId;
		return this;
	}
}
