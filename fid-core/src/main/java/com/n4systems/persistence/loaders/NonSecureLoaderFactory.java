package com.n4systems.persistence.loaders;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.asset.LastEventDateLoader;
import com.n4systems.model.downloadlink.AllDownloadLinksByDateLoader;
import com.n4systems.model.event.EventBySubEventLoader;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;

public class NonSecureLoaderFactory {

	/*
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods
	 * to this factory.
	 */

	public AllDownloadLinksByDateLoader createAllDownloadLinksByDateLoader() {
		return new AllDownloadLinksByDateLoader();
	}

	public EventBySubEventLoader createEventBySubEventLoader() {
		return new EventBySubEventLoader();
	}

	public LastEventDateLoader createLastEventDateLoader(Long networkId) {
		return new LastEventDateLoader();
	}

	public <T extends BaseEntity> NonSecureIdLoader<T> createNonSecureIdLoader(Class<T> clazz) {
		return new NonSecureIdLoader<T>(clazz);
	}
	
	public OrgConnectionExistsLoader createOrgConnectionExistsLoader() {
		return new OrgConnectionExistsLoader();
	}

}
