package com.n4systems.fieldid.api.mobile.resources.model;

import java.util.Date;

public class ApiReadWriteModel {
	private String sid;
	private Date modified;
	private boolean active;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
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
