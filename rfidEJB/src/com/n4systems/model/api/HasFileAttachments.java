package com.n4systems.model.api;

import java.util.List;

import com.n4systems.model.FileAttachment;

public interface HasFileAttachments {
	public List<FileAttachment> getAttachments();
	public void setAttachments(List<FileAttachment> attachments);
}
