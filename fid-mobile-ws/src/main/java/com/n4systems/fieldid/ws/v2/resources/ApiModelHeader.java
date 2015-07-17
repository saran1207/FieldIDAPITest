package com.n4systems.fieldid.ws.v2.resources;

import java.io.Serializable;
import java.util.Date;

public class ApiModelHeader<T> implements Serializable {
	private final T sid;
	private final Date modified;

	public ApiModelHeader(T sid, Date modified) {
		this.sid = sid;
		this.modified = modified;
	}

	public T getSid() {
		return sid;
	}

	public Date getModified() {
		return modified;
	}
}
