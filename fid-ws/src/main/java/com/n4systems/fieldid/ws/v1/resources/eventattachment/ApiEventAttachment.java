package com.n4systems.fieldid.ws.v1.resources.eventattachment;

public class ApiEventAttachment {
	private String fileName;
	private String comments;
	private byte[] image;
	private String eventSid;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getEventSid() {
		return eventSid;
	}

	public void setEventSid(String eventSid) {
		this.eventSid = eventSid;
	}	
}
