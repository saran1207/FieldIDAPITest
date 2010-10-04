package com.n4systems.model.parents.legacy;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.model.security.AllowSafetyNetworkAccess;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyEntityCreateModifyDate extends LegacyBaseEntity {
	
    @Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
    @Temporal(TemporalType.TIMESTAMP)
	private Date dateModified;

	@PrePersist
    protected void prePersist() {
    	if(dateCreated == null) {
    		dateCreated = new Date();
    	}
    	preMerge();
    }
    
	@PreUpdate
	protected void preMerge() {
		dateModified = new Date();
    }

    @AllowSafetyNetworkAccess
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

    @AllowSafetyNetworkAccess
	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	public void touch() {
		dateModified = null;
		
	}

}
