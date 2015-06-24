package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Asset;
import com.n4systems.util.ServiceLocator;
import org.apache.log4j.Logger;

import java.io.IOException;

public class DownloadAssetImage extends AbstractS3DownloadAction {
	private static final long serialVersionUID = 1L;
	
	protected Long uniqueID;
	private Asset asset;
    private S3Service s3Service = ServiceLocator.getS3Service();

    private static final Logger logger = Logger.getLogger(DownloadAssetImage.class);

    public DownloadAssetImage(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected Asset loadAsset() {
		return getLoaderFactory().createFilteredIdLoader(Asset.class).setId(uniqueID).load();
	}
	@Override
	protected boolean initializeDownload() {
		asset = loadAsset();
		
		if(asset == null || asset.getImageName() == null ) {
			addActionError(getText("error.noassettype"));
			setFailActionResult(MISSING);
			return false;
		}

		return true;
	}

	@Override
    protected byte[] getFileBytes() {
        try {
            return s3Service.downloadAssetProfileOriginalImage(asset.getId(), asset.getImageName());
        } catch (IOException e) {
            logger.error("Asset Image named " + asset.getImageName() + " for Asset with ID " + asset.getId() + " is missing.", e);
            addActionError(getText("error.noassetimage"));
            setFailActionResult(MISSING);
            return null;

        }
    }

	@Override
	public String getFileName() {
		return asset.getImageName();
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

}
