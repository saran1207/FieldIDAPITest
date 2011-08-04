package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Asset;
import com.n4systems.reporting.PathHandler;

public class DownloadAssetImage extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;
	
	protected Long uniqueID;
	private Asset asset;
	
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
	protected String onFileNotFoundException(FileNotFoundException e) {
		addActionError(getText("error.noassetimage"));
		return MISSING;
	}

	@Override
	public File getFile() {
		return PathHandler.getAssetImageFile(asset);
	}

	@Override
	public String getFileName() {
		return asset.getImageName();
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

}
