package com.n4systems.fieldid.ws.v1.resources.model;

import java.util.Date;

public abstract class ApiReadonlyModel {
	private Long sid;
	private Date modified;
	private boolean active;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
