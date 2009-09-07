package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.model.Product;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.product.ProductAttachmentListLoader;
import com.n4systems.reporting.PathHandler;

public class DownloadProductAttachmentFile extends DownloadAction {

	private static final long serialVersionUID = 1L;

	private Product product;

	public DownloadProductAttachmentFile(PersistenceManager persistenceManager) {
		super(persistenceManager);

	}

	@Override
	public String doDownload() {
		loadProduct();
		ProductAttachment attachment = loadAttachment();
		File attachedFile = findFile(attachment);
		
		return streamFile(attachedFile);
	}

	private String streamFile(File attachedFile) {
		// stream the file back to the browser
		fileSize = new Long(attachedFile.length()).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream(attachedFile);
			return sendFile(input);
		} catch (IOException e) {
			failure = true;
		} finally {

		}

		return (failure) ? ERROR : null;
	}

	private File findFile(ProductAttachment attachment) {
		File fileDirectory = PathHandler.getAttachmentFile(attachment);
		File attachedFile = new File(fileDirectory.getAbsolutePath(), attachment.getFileName());

		if (!attachedFile.exists()) {
			addActionError(getText("error.no_product_attached_file", fileName));
			throw new MissingEntityException("file does not exist on the file system. " + fileName);
		}
		return attachedFile;
	}

	private ProductAttachment loadAttachment() {
		ProductAttachment attachment = null;

		ProductAttachmentListLoader loader = getLoaderFactory().createProductAttachmentListLoader();
		loader.setProduct(product);

		for (ProductAttachment attach : loader.load()) {
			if (attach.getId().equals(attachmentID)) {
				attachment = attach;
				break;
			}
		}
		// we did not find the attachment
		if (attachment == null) {
			addActionError(getText("error.no_product_attached_file", fileName));
			throw new MissingEntityException("attachment is not attached to the loaded product.");
		}
		return attachment;
	}

	private void loadProduct() {
		product = persistenceManager.find(Product.class, uniqueID, getSecurityFilter());

		if (product == null) {
			addActionError(getText("error.noproduct"));
			throw new MissingEntityException("product could not be loaded.");
		}
	}

}
