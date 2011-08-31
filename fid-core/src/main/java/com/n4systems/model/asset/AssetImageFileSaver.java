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
	private String filePath;
	private byte[] data;
	private boolean dataSpecified; //Let's use this, just in case data was null.
	
	public AssetImageFileSaver(Asset asset, String filePath) {
		this.asset = asset;
		this.filePath = filePath;
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
		
		if(!dataSpecified) {
			// Web Site call
			File tmpFile = copyFile();
			updateFileName(tmpFile);
		} else {
			// Web Service Call
			writeFile();
		}
	}
	
	private void updateFileName(File tmpFile) {
		asset.setImageName(tmpFile.getName());
	}

	private File copyFile() throws IOException {
		File attachmentDir = PathHandler.getAssetImageDir(asset);
		File tmpDirectory = PathHandler.getTempRoot();

		File tmpFile = new File(tmpDirectory, filePath);
		
		if(attachmentDir.exists()) {
			FileUtils.cleanDirectory(attachmentDir);
		}
		FileUtils.copyFileToDirectory(tmpFile, attachmentDir);
		return tmpFile;
	}
	
	// Write to the disk from the data. (During Web Service Call)
	private void writeFile() throws IOException {
		if(data == null)
			throw new FileAttachmentException("Data cannot be null.");
		
		File attachmentDir = PathHandler.getAssetImageDir(asset);
		
		if(attachmentDir.exists()) {
			FileUtils.cleanDirectory(attachmentDir);
		}
		
		File imageFile = new File(attachmentDir, filePath);
		try {
			FileUtils.writeByteArrayToFile(imageFile, data);
		} catch(IOException e) {
			throw new FileAttachmentException("Failed to write attachment data [" + imageFile + "]", e);
		}
	}
	
	public String getImageFileName() {
		return asset.getImageName();
	}

	public void setData(byte[] data) {
		this.data = data;
		this.dataSpecified = true;
	}

	public byte[] getData() {
		return data;
	}
}
