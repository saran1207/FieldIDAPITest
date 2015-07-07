package com.n4systems.fieldid.ws.v2.resources.org;

import java.net.URL;

public class ApiOrgImage {
	private Long sid;
	private URL url;

	public ApiOrgImage() {
		this(null, null);
	}

	public ApiOrgImage(Long sid, URL url) {
		this.sid = sid;
		this.url = url;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}
