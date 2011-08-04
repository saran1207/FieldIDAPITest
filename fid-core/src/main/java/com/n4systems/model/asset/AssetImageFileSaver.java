package com.n4systems.model.asset;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Asset;
import com.n4systems.persistence.savers.FileSaver;
import com.n4systems.reporting.PathHandler;

public class AssetImageFileSaver extends FileSaver<Asset> {

	private final static Logger logger = Logger.getLogger(AssetImageFileSaver.class);
	private Asset asset;
	private byte[] data;
	
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
		
		if(data == null) {
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

		File tmpFile = new File(tmpDirectory, asset.getImageName());
		
		if(attachmentDir.exists()) {
			FileUtils.cleanDirectory(attachmentDir);
		}
		FileUtils.copyFileToDirectory(tmpFile, attachmentDir);
		return tmpFile;
	}
	
	// Write to the disk from the data. (During Web Service Call)
	private void writeFile() throws IOException {
		File attachmentDir = PathHandler.getAssetImageDir(asset);
		
		if(attachmentDir.exists()) {
			FileUtils.cleanDirectory(attachmentDir);
		}
		
		File imageFile = new File(attachmentDir, asset.getImageName());

		OutputStream out = null;
		
		try {
			out = new BufferedOutputStream(new FileOutputStream(imageFile));
			out.write(data);
		} catch(IOException e) {
			throw new FileAttachmentException("Failed to write attachment data [" + imageFile + "]", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	public String getImageFileName() {
		return asset.getImageName();
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}
}
