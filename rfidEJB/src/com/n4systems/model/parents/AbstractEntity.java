package com.n4systems.model.parents;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.HasModifiedBy;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class AbstractEntity extends BaseEntity implements Serializable, HasModifiedBy {

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	private UserBean modifiedBy;

	public AbstractEntity() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		if (created == null) {
			created = new Date();
		}
		modified = new Date();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		modified = new Date();
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

	/** Nulls the modified field.  Will force Hibernate to save on merge. */
	public void touch() {
		modified = null;
	}

	@Override
	public int hashCode() {
		if (!isNew()) {
			return id.hashCode();
		}
		return super.hashCode();
	}

}
