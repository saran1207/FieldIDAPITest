package com.n4systems.model.product;

import java.io.File;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.reporting.PathHandler;

@Deprecated
public class ProductAttachmentDeleter {

	private PersistenceManager pm;
	
	public ProductAttachmentDeleter(PersistenceManager pm) {
		super();
		this.pm = pm;
	}
	
	public void delete(ProductAttachment attachment) {
		deleteAttachment(attachment);
		deleteFile(attachment);
	}

	private void deleteAttachment(ProductAttachment attachment) {
		if (attachment == null || attachment.isNew()) {
			throw new InvalidArgumentException("you need an attachment that has been persisted.");
		}
		
		pm.delete(attachment);
	}
	
	//TODO move to a file Deleter.
	private void deleteFile(ProductAttachment attachment) {
		File attachmentDirectory = PathHandler.getAttachmentFile(attachment);
		File attachedFile = new File(attachmentDirectory, attachment.getFileName());
		
		if (attachedFile.exists()) {
			attachedFile.delete();
		}
		
	}
}
