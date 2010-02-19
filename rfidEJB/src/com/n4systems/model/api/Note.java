package com.n4systems.model.api;

import javax.activation.FileTypeMap;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.n4systems.model.Attachment;


@Embeddable
public class Note implements Attachment {
	private static final long serialVersionUID = 1L;
	
	private String fileName;
	@Column(name="comment")
	private String comments;
	
	public Note() {
		this(null, null);
	}
	
	public Note(String fileName, String comments) {
		this.fileName = fileName;
		this.comments = comments;
	}
	
	public Note(Note note) {
		this(note.getFileName(), note.getComments());
	}
	
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
	
	/**
	 * Tests if the filename has a content type starting with <code>'image/'</code>. The
	 * content type is queried from {@link FileTypeMap#getContentType(String)}.
	 * 
	 * @param fileName	String file name, including extension.
	 * @return			<code>true</code> if content type starts with <code>'image/'</code>.
	 */
	public boolean isImage() {
		if (hasAttachedFile()) {
			String contentType = getContentType(fileName);
			return contentType.startsWith("image/");
		}
		return false;
	}

	protected String getContentType(String fileName) {
		return FileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
	}

	public boolean hasAttachedFile() {
		return fileName != null && !fileName.isEmpty();
	}

}
