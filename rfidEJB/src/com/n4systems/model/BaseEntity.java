package com.n4systems.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class BaseEntity implements Saveable, Serializable {

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(BaseEntity.class);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
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

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isNew() {
		return (id == null);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Long getId() {
		return id;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Object getIdentifier() {
		return getId();
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/** Alias for {@link #getId()}.  Used by Struts/Freemarker */
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
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
}
