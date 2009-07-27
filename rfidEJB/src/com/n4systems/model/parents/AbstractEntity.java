package com.n4systems.model.parents;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.model.api.HasModifiedBy;

import rfid.ejb.entity.UserBean;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class AbstractEntity implements Serializable, HasModifiedBy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	private UserBean modifiedBy;

	@SuppressWarnings("unused")
	@PrePersist
	private void prePersist() {
		if (created == null) {
			created = new Date();
		}
		modified = new Date();
		onCreate();
	}

	@SuppressWarnings("unused")
	@PreUpdate
	private void preMerge() {
		modified = new Date();
		onUpdate();
	}

	@SuppressWarnings("unused")
	@PostLoad
	private void postLoad() {
		onLoad();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * methods here as alias' so that struts will assign values for us.
	 * 
	 * @return
	 */
	public Long getID() {
		return getId();
	}

	/**
	 * methods here as alias' so that struts will assign values for us.
	 * 
	 * @return
	 */
	public void setID(Long id) {
		setId(id);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date dateCreated) {
		this.created = dateCreated;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date dateModified) {
		this.modified = dateModified;
	}
	
		
	public UserBean getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserBean modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * this method will force the entity to be saved when you merge it.
	 */
	public void touch() {
		modified = null;
	}

	protected void onLoad() {
	}

	protected void onUpdate() {
	}

	protected void onCreate() {
	}

	@Override
	public boolean equals(Object obj) {

		if (obj != null && obj.getClass().isInstance(this)) {
			AbstractEntity entity = (AbstractEntity) obj;
			if (entity.id != null) {
				return entity.id.equals(this.id);
			} else {
				return super.equals(obj);
			}
		}
		return false;
	}

	public boolean isNew() {
		return (id == null);
	}

}
