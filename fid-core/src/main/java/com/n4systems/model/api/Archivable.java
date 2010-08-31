package com.n4systems.model.api;

public interface Archivable extends Retirable {

	public enum EntityState {
		ACTIVE(), RETIRED(), ARCHIVED( );
	}
	
	public void activateEntity();

	public void archiveEntity();

	public EntityState getEntityState() ;
		
	public void retireEntity() ;
	
	public boolean isActive();
	
	public boolean isArchived();
}
