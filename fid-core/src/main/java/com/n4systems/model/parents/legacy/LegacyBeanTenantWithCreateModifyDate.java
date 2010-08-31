package com.n4systems.model.parents.legacy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

// this class this just temporary while we refactor the legacy beans to use the new AbstractEntity
// Once a been is moved to it's new version, OrganizationAndTenant should be replaced with EntityWithTenant

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBeanTenantWithCreateModifyDate extends LegacyEntityCreateModifyDate implements HasTenant {
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(LegacyBeanTenantWithCreateModifyDate.class);
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tenant_id")
	private Tenant tenant;
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Tenant getTenant() {
		return tenant;
	}
	
	public void setTenant( Tenant tenant) {
		this.tenant = tenant;
	}
	
}
