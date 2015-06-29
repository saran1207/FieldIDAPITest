package com.n4systems.fieldid.api.mobile.resources;

import java.io.Serializable;
import java.util.Date;

public class ApiModelHeader implements Serializable {
	private String sid;
	private Date modified;

	public ApiModelHeader() {}

	public ApiModelHeader(String sid, Date modified) {
		this.sid = sid;
		this.modified = modified;
	}

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
}
