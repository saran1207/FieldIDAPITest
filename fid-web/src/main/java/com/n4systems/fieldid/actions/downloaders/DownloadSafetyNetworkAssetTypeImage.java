package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AssetType;

public class DownloadSafetyNetworkAssetTypeImage extends DownloadAssetTypeImage {
	private static final long serialVersionUID = 1L;
	
	private Long networkId;
	
	public DownloadSafetyNetworkAssetTypeImage(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected AssetType loadAssetType() {
		return getLoaderFactory().createSafetyNetworkAssetTypeLoader().setAssetTypeId(uniqueID).setAssetNetworkId(networkId).load();
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}
}
