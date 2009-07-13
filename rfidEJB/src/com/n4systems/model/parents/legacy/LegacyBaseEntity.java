package com.n4systems.model.parents.legacy;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBaseEntity implements Serializable {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueID;

	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	@Override
	public boolean equals(Object obj) {
		
		if( obj != null  && obj.getClass().isInstance(this)) {
			if( this.uniqueID.equals( ((LegacyBaseEntity)obj).uniqueID ) ) {
				return true;
			}
		}
		
		return false;
	}
    
	
	
}
