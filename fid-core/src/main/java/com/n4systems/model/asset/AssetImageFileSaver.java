package com.n4systems.model.asset;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Asset;
import com.n4systems.persistence.savers.FileSaver;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class AssetImageFileSaver extends FileSaver<Asset> {

	private final static Logger logger = Logger.getLogger(AssetImageFileSaver.class);
	private Asset asset;
	private String filePath;
	private byte[] data;
	private boolean dataSpecified; //Let's use this, just in case data was null.

    private S3Service s3Service = ServiceLocator.getS3Service();

	public AssetImageFileSaver(Asset asset, String filePath) {
		this.asset = asset;
		this.filePath = filePath;
	}
	
	@Override
	public void remove() {
        s3Service.removeAssetProfileImage(asset.getId(), filePath);
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
		File tmpFile = new File(PathHandler.getTempRoot(), filePath);

        s3Service.uploadAssetProfileImage(tmpFile, asset.getId(), tmpFile.getName());
		
		return tmpFile;
	}
	
	// Write to the disk from the data. (During Web Service Call)
	private void writeFile() throws IOException {
		if(data == null)
			throw new FileAttachmentException("Data cannot be null.");
				
		File imageFile = new File(PathHandler.getTempRoot(), getImageFileName());
		try {
			FileUtils.writeByteArrayToFile(imageFile, data);
            s3Service.uploadAssetProfileImage(imageFile, asset.getId(), imageFile.getName());
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
