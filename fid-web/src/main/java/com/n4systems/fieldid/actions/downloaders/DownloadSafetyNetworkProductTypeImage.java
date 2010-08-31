package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ProductType;

public class DownloadSafetyNetworkProductTypeImage extends DownloadProductTypeImage {
	private static final long serialVersionUID = 1L;
	
	private Long networkId;
	
	public DownloadSafetyNetworkProductTypeImage(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected ProductType loadProductType() {
		return getLoaderFactory().createSafetyNetworkProductTypeLoader().setProductTypeId(uniqueID).setProductNetworkid(networkId).load();
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}
}
