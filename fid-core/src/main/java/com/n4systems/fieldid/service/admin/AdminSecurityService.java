package com.n4systems.fieldid.service.admin;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.concurrent.Callable;

public class AdminSecurityService extends FieldIdPersistenceService {

	public <T> T executeUnderTenant(long tenantId, Callable<T> callable) {
		/*
			We need to cache and reset the security context to avoid nested calls wiping it out.
			If either the user or tenant filter are null, they will throw a SecurityException which
			makes this a little kludgy
		 */
		SecurityFilter tenantFilter = null;
		try { tenantFilter = securityContext.getTenantSecurityFilter(); } catch (SecurityException e) {}

		UserSecurityFilter userFilter = null;
		try { userFilter = securityContext.getUserSecurityFilter(); } catch (SecurityException e) {}

		try {
			securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(tenantId));

			PrimaryOrg primaryOrg = getPrimaryOrgForTenant(tenantId);
			securityContext.setUserSecurityFilter(new UserSecurityFilter(primaryOrg, null, null));

			return callable.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			securityContext.setTenantSecurityFilter(tenantFilter);
			securityContext.setUserSecurityFilter(userFilter);
		}
	}

	private PrimaryOrg getPrimaryOrgForTenant(Long tenantId) {
		QueryBuilder<PrimaryOrg> query = createTenantSecurityBuilder(PrimaryOrg.class);
		query.addSimpleWhere("tenant.id", tenantId);
		return persistenceService.find(query);
	}

}
