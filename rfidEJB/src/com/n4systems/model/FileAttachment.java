package com.n4systems.model;

import javax.activation.FileTypeMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "fileattachments")
public class FileAttachment extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	@Column(length=255)
	private String fileName;
	
	
	private String comments;
	
	public FileAttachment() {}

	public FileAttachment(Tenant tenant, UserBean modifiedBy, String fileName) {
		super(tenant);
		setModifiedBy(modifiedBy);
		setFileName(fileName);
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
