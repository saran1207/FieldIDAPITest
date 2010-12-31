package com.n4systems.model.orgs;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AllOrgsWithArchivedListLoader extends ListLoader<BaseOrg> {

	public AllOrgsWithArchivedListLoader(TenantOnlySecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<BaseOrg> load(EntityManager em, SecurityFilter filter) {
		TenantOnlySecurityFilter tenantFilter = (TenantOnlySecurityFilter)filter;
		tenantFilter.enableShowArchived();
		
		QueryBuilder<BaseOrg> builder = new QueryBuilder<BaseOrg>(BaseOrg.class, tenantFilter);
		List<BaseOrg> orgs = builder.getResultList(em);
		return orgs;
	}

}
