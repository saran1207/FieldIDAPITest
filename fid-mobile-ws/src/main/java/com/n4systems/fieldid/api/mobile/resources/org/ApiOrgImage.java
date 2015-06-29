package com.n4systems.fieldid.api.mobile.resources.org;

public class ApiOrgImage {
	private Long sid;
	private String path;

	public ApiOrgImage() {
		this(null, null);
	}

	public ApiOrgImage(Long sid, String path) {
		this.sid = sid;
		this.path = path;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
