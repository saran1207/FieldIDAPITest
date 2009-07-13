package com.n4systems.model.api;

import javax.persistence.Embeddable;


@Embeddable
public class Note {
	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private String comment;
	
	public Note() {
		this(null, null);
	}
	
	public Note(String fileName, String comment) {
		this.fileName = fileName;
		this.comment = comment;
	}
	
	public Note(Note note) {
		this(note.getFileName(), note.getComment());
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
