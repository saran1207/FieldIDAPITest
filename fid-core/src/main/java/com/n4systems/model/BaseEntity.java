package com.n4systems.model;

import com.google.common.collect.Maps;
import com.n4systems.model.api.Copyable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.persistence.localization.LocaleUserType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("serial")
@MappedSuperclass
@TypeDefs({
        @TypeDef(name="localize", defaultForType=Locale.class, typeClass=LocaleUserType.class)
        })
abstract public class BaseEntity implements Saveable, Serializable, Copyable {

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(BaseEntity.class);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

    private @Transient Map<String,Object> translations;

	public BaseEntity() {}
	
	public BaseEntity(Long id) {
		this.id = id;
	}
	
	
	@SuppressWarnings("unused")
	@PrePersist
	private void prePersist() {
		onCreate();
	}

	@SuppressWarnings("unused")
	@PreUpdate
	private void preMerge() {
		onUpdate();
	}

	@SuppressWarnings("unused")
	@PostLoad
	private void postLoad() {
		onLoad();
	}
	
	protected void onLoad() {}

	protected void onUpdate() {}

	protected void onCreate() {}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass().isInstance(this) || getClass().isInstance(obj)) {
			BaseEntity entity = (BaseEntity)obj;
			if (entity.getId() != null) {
				return entity.getId().equals(getId());
			} else {
				return super.equals(obj);
			}
		}
		return false;
	}
	
	@Override
    public int hashCode() {
        if (!isNew()) {
                return getId().hashCode();
        }
        return super.hashCode();
    }

	@AllowSafetyNetworkAccess
	public boolean isNew() {
		return (id == null);
	}
	
	@AllowSafetyNetworkAccess
	public Long getId() {
		return id;
	}
	
    @AllowSafetyNetworkAccess
    public Object getEntityId() {
        return getId();
    }

	public void setId(Long id) {
		this.id = id;
	}
	
	/** Alias for {@link #getId()}.  Used by Struts/Freemarker */
	@AllowSafetyNetworkAccess
	public Long getID() {
		return getId();
	}

	/** Alias for {@link #setId(Long)}.  Used by Struts/Freemarker */
	public void setID(Long id) {
		setId(id);
	}
	
	protected <T> T enhance(SecurityEnhanced<T> entity, SecurityLevel level) {
		return EntitySecurityEnhancer.enhance(entity, level);
	}

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public void reset() {
    	id = null;
    }

    public boolean isTranslated() {
        return translations!=null;
    }

    @Override
    // good god, what a hack...only done to make select & combo criteria work.
    public void setUntranslatedValue(String fieldName, Object value) {
        if (translations==null) {
            translations = Maps.newHashMap();
        }
        translations.put(fieldName,value);
    }

    @Override
    public Map<String, Object> getTranslatedValues() {
        return translations;
    }

    public void markDirty() {
        // just a placeholder so we know something has changed. we don't care what field or what value.
        setUntranslatedValue("VOID","PLACEHOLDER-VALUE");
    }

}
