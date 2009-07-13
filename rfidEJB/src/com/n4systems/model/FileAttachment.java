package com.n4systems.model;

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

	public FileAttachment(TenantOrganization tenant, UserBean modifiedBy, String fileName) {
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
}
