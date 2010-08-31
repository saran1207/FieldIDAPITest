package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.reporting.PathHandler;

public class DownloadProductAttachmentFile extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;

	protected Long attachmentID;
	private ProductAttachment attachment;
	
	public DownloadProductAttachmentFile(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected ProductAttachment loadProductAttachment() {
		return getLoaderFactory().createFilteredIdLoader(ProductAttachment.class).setId(attachmentID).load();
	}
	
	@Override
	protected boolean initializeDownload() {
		attachment = loadProductAttachment();

		if (attachment == null) {
			addActionError(getText("error.no_product_attached_file"));
			setFailActionResult(MISSING);
			return false;
		}
		
		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		addActionError(getText("error.no_product_attached_file", attachment.getFileName()));
		return MISSING;
	}

	@Override
	public File getFile() {
		return PathHandler.getProductAttachmentFile(attachment);
	}

	@Override
	public String getFileName() {
		return attachment.getFileName();
	}

	public void setAttachmentID(Long attachmentID) {
		this.attachmentID = attachmentID;
	}
	
	// required by common/_attachedFilesList.ftl but not used here
	public void setUniqueID(Long uniqueID) {}

}
