package com.n4systems.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class BaseEntity implements Serializable {

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

		if (obj != null && obj.getClass().isInstance(this)) {
			BaseEntity entity = (BaseEntity)obj;
			if (entity.id != null) {
				return entity.id.equals(this.id);
			} else {
				return super.equals(obj);
			}
		}
		return false;
	}
	
	@Override
    public int hashCode() {
        if (!isNew()) {
                return id.hashCode();
        }
        return super.hashCode();
    }

	public boolean isNew() {
		return (id == null);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/** Alias for {@link #getId()}.  Used by Struts/Freemarker */
	public Long getID() {
		return getId();
	}

	/** Alias for {@link #setId(Long)}.  Used by Struts/Freemarker */
	public void setID(Long id) {
		setId(id);
	}
}
