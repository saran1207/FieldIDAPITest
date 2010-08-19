package com.n4systems.model.parents;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.SecurityDefiner;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class ArchivableEntityWithTenant extends EntityWithTenant implements Archivable {

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(ArchivableEntityWithTenant.class);
	}
	
	@Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
	
	public ArchivableEntityWithTenant() {
		this(null);
	}

	public ArchivableEntityWithTenant(Tenant tenant) {
		super(tenant);
	}

	public void activateEntity() {
		state = EntityState.ACTIVE;
	}

	public void archiveEntity() {
		state = EntityState.ARCHIVED;
	}

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
	
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
	
	public void setState(EntityState state) {
		this.state = state;
	}
}
