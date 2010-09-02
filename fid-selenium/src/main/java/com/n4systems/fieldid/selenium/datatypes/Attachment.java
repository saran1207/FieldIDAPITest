package com.n4systems.fieldid.selenium.datatypes;

public class Attachment {
	String filename;
	String comment;
	
	public Attachment(String filename, String comment) {
		this.filename = filename;
		this.comment = comment;
	}
	
	public void setFileName(String filename) {
		this.filename = filename;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getFileName() {
		return this.filename;
	}
	
	public String getComment() {
		return this.comment;
	}
}
