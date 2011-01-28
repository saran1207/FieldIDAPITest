package com.n4systems.model.lastmodified;

import java.util.Date;

public class LastModified {
	private final long id;
	private final Date modified;
	
	public LastModified(long id, Date modified) {
		this.id = id;
		this.modified = modified;
	}

	public long getId() {
		return id;
	}

	public Date getModified() {
		return modified;
	}
}
