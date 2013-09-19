package com.n4systems.model.parents.legacy;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBaseEntity implements Serializable, Saveable {
    
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(LegacyBaseEntity.class);
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueID;

    private @Transient boolean translated = false;

	@AllowSafetyNetworkAccess
	public Long getUniqueID() {
		return uniqueID;
	}
	
    @AllowSafetyNetworkAccess
    public Object getEntityId() {
        return getUniqueID();
    }

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if(obj.getClass().isInstance(this) || getClass().isInstance(obj)) {
			LegacyBaseEntity otherBase = (LegacyBaseEntity)obj;
			if (otherBase.getUniqueID() != null) {
				return otherBase.getUniqueID().equals(getUniqueID());
			} else {
				return super.equals(obj);
			}
		}
		return false;
	}
    
	@AllowSafetyNetworkAccess
	public boolean isNew() {
		return (uniqueID == null);
	}
	
	protected <T> T enhance(SecurityEnhanced<T> entity, SecurityLevel level) {
		return EntitySecurityEnhancer.enhance(entity, level);
	}


    @Override
    public boolean isTranslated() {
        return translated;
    }

    @Override
    public void setTranslated(boolean translated) {
        this.translated = translated;
    }
}
