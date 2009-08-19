package com.n4systems.model.parents;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.security.TenantField;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class EntityWithTenant extends AbstractEntity implements HasTenant {
	public static final String columnName = "tenant_id";
	protected static final String TENANT_ID_FIELD = "tenant.id";
	
	@TenantField
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = columnName)
	private Tenant tenant;
	
	public EntityWithTenant() {}
	
	public EntityWithTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
	public Tenant getTenant() {
		return tenant;
	}
	
	public void setTenant( Tenant tenant) {
		this.tenant = tenant;
	}
	
}
