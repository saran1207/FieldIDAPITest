package com.n4systems.model.parents.legacy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.security.SecurityDefiner;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class LegacyBeanTenant extends LegacyBaseEntity implements HasTenant {
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(LegacyBeanTenant.class);
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tenant_id")
	private Tenant tenant;
	
	public Tenant getTenant() {
		return tenant;
	}
	
	public void setTenant( Tenant tenant) {
		this.tenant = tenant;
	}
}
