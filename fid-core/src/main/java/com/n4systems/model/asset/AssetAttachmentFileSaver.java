package com.n4systems.model.asset;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.persistence.savers.FileSaver;
import com.n4systems.reporting.PathHandler;

public class AssetAttachmentFileSaver extends FileSaver<AssetAttachment> {
	private static final Logger logger = Logger.getLogger(AssetAttachmentFileSaver.class);
	private AssetAttachment attachment;
	
	@Override
	public void save() {
		try {
			copyAttachment();
		} catch (IOException e) {
			logger.error("failed to copy uploaded file ", e);
			throw new FileAttachmentException(e);
		}
	}

	private void copyAttachment() throws IOException {
		if (attachment == null) {
			throw new InvalidArgumentException("you must give an attachment");
		}
		
		File tmpFile = copyFile();
		updateFileName(tmpFile);
	}

	private void updateFileName(File tmpFile) {
		attachment.setFileName(tmpFile.getName());
	}

	private File copyFile() throws IOException {
		File attachmentDir = PathHandler.getAssetAttachmentDir(attachment);
		File tmpDirectory = PathHandler.getTempRoot();

		File tmpFile = new File(tmpDirectory, attachment.getFileName());
		FileUtils.copyFileToDirectory(tmpFile, attachmentDir);
		return tmpFile;
	}

	@Override
	public void remove() {
	}

	public AssetAttachmentFileSaver setAttachment(AssetAttachment attachment) {
		this.attachment = attachment;
		return this;
	}

}
