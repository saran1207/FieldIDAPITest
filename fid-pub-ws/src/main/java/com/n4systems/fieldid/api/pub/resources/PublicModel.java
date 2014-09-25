package com.n4systems.fieldid.api.pub.resources;

import java.io.Serializable;

public class PublicModel implements Serializable {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
