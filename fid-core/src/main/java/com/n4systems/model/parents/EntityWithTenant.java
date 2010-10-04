package com.n4systems.model.parents;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class EntityWithTenant extends AbstractEntity implements HasTenant {
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(EntityWithTenant.class);
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tenant_id")
	private Tenant tenant;
	
	public EntityWithTenant() {
		this(null);
	}
	
	public EntityWithTenant(Tenant tenant) {
		super();
		this.tenant = tenant;
	}
	
	@AllowSafetyNetworkAccess
	public Tenant getTenant() {
		return tenant;
	}
	
	public void setTenant( Tenant tenant) {
		this.tenant = tenant;
	}
	
}
