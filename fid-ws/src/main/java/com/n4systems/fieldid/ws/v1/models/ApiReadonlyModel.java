package com.n4systems.fieldid.ws.v1.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class ApiReadonlyModel {
	private Long id;
	private boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
