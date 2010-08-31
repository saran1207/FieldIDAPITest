package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ProductType;
import com.n4systems.reporting.PathHandler;

public class DownloadProductTypeImage extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;
	
	protected Long uniqueID;
	private ProductType productType;
	
	public DownloadProductTypeImage(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected ProductType loadProductType() {
		return getLoaderFactory().createProductTypeLoader().setId(uniqueID).load();
	}
	
	@Override
	protected boolean initializeDownload() {
		productType = loadProductType();
		
		if(productType == null || !productType.hasImage()) {
			addActionError(getText("error.noproducttype"));
			setFailActionResult(MISSING);
			return false;
		}

		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		addActionError(getText("error.noproductimage"));
		return MISSING;
	}

	@Override
	public File getFile() {
		return new File(PathHandler.getProductTypeImageFile(productType), productType.getImageName());
	}

	@Override
	public String getFileName() {
		return productType.getImageName();
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

}
