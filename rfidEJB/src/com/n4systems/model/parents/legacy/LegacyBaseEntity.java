package com.n4systems.model.parents.legacy;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBaseEntity implements Serializable {
    
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(LegacyBaseEntity.class);
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueID;

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Long getUniqueID() {
		return uniqueID;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Object getIdentifier() {
		return getUniqueID();
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
    
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isNew() {
		return (uniqueID == null);
	}
	
	protected <T> T enhance(SecurityEnhanced<T> entity, SecurityLevel level) {
		return EntitySecurityEnhancer.enhance(entity, level);
	}
}
