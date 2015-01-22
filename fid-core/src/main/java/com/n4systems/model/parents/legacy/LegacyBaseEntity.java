package com.n4systems.model.parents.legacy;

import com.google.common.collect.Maps;
import com.n4systems.model.PublicIdEncoder;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBaseEntity implements Serializable, Saveable {
    
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(LegacyBaseEntity.class);
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueID;

    private @Transient
    Map<String,Object> translations = null;

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
        return translations!=null;
    }

    @Override
    public void setUntranslatedValue(String name, Object value) {
        if (translations==null) {
            translations = Maps.newHashMap();
        }
        translations.put(name,value);
    }

    @Override
    public Map<String, Object> getTranslatedValues() {
        return translations;
    }

	public String getPublicId() {
		return PublicIdEncoder.encode(uniqueID);
	}

	public void setPublicId(String publicId) {
		uniqueID = PublicIdEncoder.decode(publicId);
	}
}
