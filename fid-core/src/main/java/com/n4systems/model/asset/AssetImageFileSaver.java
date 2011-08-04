package com.n4systems.model.asset;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Asset;
import com.n4systems.persistence.savers.FileSaver;
import com.n4systems.reporting.PathHandler;

public class AssetImageFileSaver extends FileSaver<Asset> {

	private final static Logger logger = Logger.getLogger(AssetImageFileSaver.class);
	private Asset asset;
	
	public AssetImageFileSaver(Asset asset) {
		this.asset = asset;
	}
	
	@Override
	public void remove() {
		File attachmentDir = PathHandler.getAssetImageDir(asset);
		
		try {
			if(attachmentDir.exists()) {
				FileUtils.cleanDirectory(attachmentDir);
			}
		} catch (IOException e) {
			logger.error("failed to remove image file ", e);
			throw new FileAttachmentException(e);
		}	
	}

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
		if (asset == null) {
			throw new InvalidArgumentException("you must give an asset");
		}
		
		File tmpFile = copyFile();
		updateFileName(tmpFile);
	}
	
	private void updateFileName(File tmpFile) {
		asset.setImageName(tmpFile.getName());
	}

	private File copyFile() throws IOException {
		File attachmentDir = PathHandler.getAssetImageDir(asset);
		File tmpDirectory = PathHandler.getTempRoot();

		File tmpFile = new File(tmpDirectory, asset.getImageName());
		
		if(attachmentDir.exists()) {
			FileUtils.cleanDirectory(attachmentDir);
		}
		FileUtils.copyFileToDirectory(tmpFile, attachmentDir);
		return tmpFile;
	}
	
	public String getImageFileName() {
		return asset.getImageName();
	}
}
