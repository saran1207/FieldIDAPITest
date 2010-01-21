package com.n4systems.model.fileattachment;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class FileAttachmentLoader extends FilteredIdLoader<FileAttachment> {

	public FileAttachmentLoader(SecurityFilter filter) {
		super(filter, FileAttachment.class);
	}

}
