package com.n4systems.model.parents;

import com.n4systems.model.api.Saveable;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@MappedSuperclass
public class AbstractStringIdEntity implements Serializable, Saveable {
	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@SuppressWarnings("unused")
	@PrePersist
	private void prePersist() {
		if (created == null) {
			created = new Date();
		}
		
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		
		modified = new Date();
	}

	@SuppressWarnings("unused")
	@PreUpdate
	private void preMerge() {
		modified = new Date();
	}

	public boolean isNew() {
	    return (id == null);
    }
	
	public String getId() {
    	return id;
    }
	
	public Object getIdentifier() {
		return getId();
	}

	public void setId(String id) {
    	this.id = id;
    }

	public Date getCreated() {
    	return created;
    }

	public void setCreated(Date created) {
    	this.created = created;
    }

	public Date getModified() {
    	return modified;
    }

	public void setModified(Date modified) {
    	this.modified = modified;
    }

}
