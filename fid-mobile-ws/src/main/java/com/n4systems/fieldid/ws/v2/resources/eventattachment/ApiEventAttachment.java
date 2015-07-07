package com.n4systems.fieldid.ws.v2.resources.eventattachment;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadWriteModel;

import java.net.URL;

public class ApiEventAttachment extends ApiReadWriteModel {
	private String comments;
	private URL url;
	private String eventSid;
	private String mimeType;
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getEventSid() {
		return eventSid;
	}

	public void setEventSid(String eventSid) {
		this.eventSid = eventSid;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
