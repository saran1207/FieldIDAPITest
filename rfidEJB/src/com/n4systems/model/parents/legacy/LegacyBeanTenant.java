package com.n4systems.model.parents.legacy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.TenantOrganization;
import com.n4systems.model.api.HasTenant;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class LegacyBeanTenant extends LegacyBaseEntity implements HasTenant {
	public static final String columnName = "r_tenant";
	
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
