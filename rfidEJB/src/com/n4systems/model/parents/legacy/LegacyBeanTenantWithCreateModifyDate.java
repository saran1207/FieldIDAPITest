package com.n4systems.model.parents.legacy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.TenantOrganization;
import com.n4systems.model.api.HasTenant;

// this class this just temporary while we refactor the legacy beans to use the new AbstractEntity
// Once a been is moved to it's new version, OrganizationAndTenant should be replaced with EntityWithTenant

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBeanTenantWithCreateModifyDate extends LegacyEntityCreateModifyDate implements HasTenant {
	public static final String columnName = "r_tenant";
	public static final String TENANT_ID_FIELD = "tenant.id";
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = columnName)
	private TenantOrganization tenant;
	
	public TenantOrganization getTenant() {
		return tenant;
	}
	
	public void setTenant( TenantOrganization tenant) {
		this.tenant = tenant;
	}
	
}
