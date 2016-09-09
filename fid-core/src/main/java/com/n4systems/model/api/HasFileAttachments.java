package com.n4systems.model.api;

import com.n4systems.model.FileAttachment;

import java.util.List;

public interface HasFileAttachments {
	public List<FileAttachment> getAttachments();
	public void setAttachments(List<FileAttachment> attachments);
}
