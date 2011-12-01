package com.n4systems.ws.resources;


import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.org.WsOrg;
import com.n4systems.ws.model.org.WsOrgConverter;

public class OrgResourceDefiner implements ResourceDefiner<BaseOrg, WsOrg> {

	@Override
	public WsModelConverter<BaseOrg, WsOrg> getResourceConverter() {
		return new WsOrgConverter();
	}

	@Override
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(BaseOrg.class);
	}
	
	@Override
	public IdLoader<? extends Loader<BaseOrg>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(BaseOrg.class).setPostFetchFields("secondaryOrg.id", "customerOrg.id", "divisionOrg.id");
	}

	@Override
	public Class<WsOrg> getWsModelClass() {
		return WsOrg.class;
	}

}
