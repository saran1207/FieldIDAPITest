package com.n4systems.model.parents;

import com.n4systems.model.TenantOrganization;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.security.TenantField;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class EntityWithTenant extends AbstractEntity implements HasTenant {
	public static final String columnName = "r_tenant";
	protected static final String TENANT_ID_FIELD = "tenant.id";
	
	@TenantField
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = columnName)
	private TenantOrganization tenant;
	
	public EntityWithTenant() {}
	
	public EntityWithTenant(TenantOrganization tenant) {
		this.tenant = tenant;
	}
	
	public TenantOrganization getTenant() {
		return tenant;
	}
	
	public void setTenant( TenantOrganization tenant) {
		this.tenant = tenant;
	}
	
}
