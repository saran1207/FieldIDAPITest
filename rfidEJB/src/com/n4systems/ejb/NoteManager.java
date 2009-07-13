package com.n4systems.ejb;

import javax.ejb.Local;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Project;

@Local
public interface NoteManager {

	public FileAttachment attachNote( FileAttachment note, Project project, Long modifiedBy ) throws FileAttachmentException;
	public int detachNote( FileAttachment note, Project project, Long modifiedBy ) throws FileAttachmentException;
}
