package com.n4systems.model.parents;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class EntityWithOwner extends EntityWithTenant implements HasOwner {
	public static final String OWNER_PATH = "owner";
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(EntityWithOwner.class);
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	private BaseOrg owner;
	
	public EntityWithOwner() {
		this(null, null);
	}
	
	public EntityWithOwner(Tenant tenant, BaseOrg owner) {
		super(tenant);
		this.owner = owner;
	}
	
	@AllowSafetyNetworkAccess
	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

}
