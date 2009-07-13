package com.n4systems.model.api;

import java.util.Set;

import com.n4systems.model.FileAttachment;

public interface HasFileAttachments {
	public Set<FileAttachment> getAttachments();
	public void setAttachments(Set<FileAttachment> attachments);
}
