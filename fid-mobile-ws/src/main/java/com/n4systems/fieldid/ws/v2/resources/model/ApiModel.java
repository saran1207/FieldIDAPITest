package com.n4systems.fieldid.ws.v2.resources.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.n4systems.fieldid.ws.v2.resources.ApiKey;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ApiModel<T> implements ApiKey<T> {
	private T sid;
	private Date modified;
	private boolean active;

	public T getSid() {
		return sid;
	}

	public void setSid(T sid) {
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
