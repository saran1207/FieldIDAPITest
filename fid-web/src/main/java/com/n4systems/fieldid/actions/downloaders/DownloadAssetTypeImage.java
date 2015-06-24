package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.AssetType;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;

import java.io.File;
import java.io.FileNotFoundException;

public class DownloadAssetTypeImage extends AbstractLegacyDownloadAction {
	private static final long serialVersionUID = 1L;
	
	protected Long uniqueID;
	private AssetType assetType;
    private S3Service s3Service = ServiceLocator.getS3Service();
	
	public DownloadAssetTypeImage(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected AssetType loadAssetType() {
		return getLoaderFactory().createAssetTypeLoader().setId(uniqueID).load();
	}
	
	@Override
	protected boolean initializeDownload() {
		assetType = loadAssetType();
		
		if(assetType == null || !assetType.hasImage()) {
			addActionError(getText("error.noassettype"));
			setFailActionResult(MISSING);
			return false;
		}

		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		addActionError(getText("error.noassetimage"));
		return MISSING;
	}

	@Override
	public File getFile() {
        File assetTypeImageFile = new File(PathHandler.getAssetTypeImageFile(assetType), assetType.getImageName());
        if(assetTypeImageFile.exists()){
            return assetTypeImageFile;
        }
        else if(s3Service.assetTypeProfileImageExists(assetType)){
            return s3Service.downloadAssetTypeProfileImage(assetType);
        }
        else {
            return null;
        }
	}

	@Override
	public String getFileName() {
		return assetType.getImageName();
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

}
