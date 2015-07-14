package com.n4systems.fieldid.ws.v2.resources;

import java.io.Serializable;
import java.util.Date;

public class ApiModelHeader implements Serializable {
	private final String sid;
	private final Date modified;

	public ApiModelHeader(String sid, Date modified) {
		this.sid = sid;
		this.modified = modified;
	}

	public String getSid() {
		return sid;
	}

	public Date getModified() {
		return modified;
	}
}
