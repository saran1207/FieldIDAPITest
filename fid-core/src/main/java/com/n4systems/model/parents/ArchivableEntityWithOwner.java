package com.n4systems.model.parents;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.SecurityDefiner;

@SuppressWarnings("serial")
@MappedSuperclass
public class ArchivableEntityWithOwner extends EntityWithOwner implements Archivable {

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(ArchivableEntityWithOwner.class);
	}
	
	@Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
	
	public ArchivableEntityWithOwner() {
		this(null, null);
	}

	public ArchivableEntityWithOwner(Tenant tenant, BaseOrg owner) {
		super(tenant, owner);
	}

	public void activateEntity() {
		state = EntityState.ACTIVE;
	}

	public void archiveEntity() {
		state = EntityState.ARCHIVED;
	}

	@AllowSafetyNetworkAccess
	public EntityState getEntityState() {
		return state;
	}

	public void retireEntity() {
		state = EntityState.RETIRED;
	}
	
	public void setRetired( boolean retired ) {
		if( retired ) {
			retireEntity();
		} else  {
			activateEntity();
		}
	}
	
	@AllowSafetyNetworkAccess
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	@AllowSafetyNetworkAccess
	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	@AllowSafetyNetworkAccess
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
}
