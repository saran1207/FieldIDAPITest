package com.n4systems.model.parents;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

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

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
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
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
}
