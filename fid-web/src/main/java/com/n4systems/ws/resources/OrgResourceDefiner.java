package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.org.WsOrg;
import com.n4systems.ws.model.org.WsOrgConverter;

public class OrgResourceDefiner implements ResourceDefiner<BaseOrg, WsOrg>{

	@Override
	public Class<WsOrg> getWsModelClass() {
		return WsOrg.class;
	}

	@Override
	public WsModelConverter<BaseOrg, WsOrg> getResourceConverter() {
		return new WsOrgConverter();
	}

	@Override
	public Loader<List<BaseOrg>> getResourceListLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createAllOrgsWithArchivedListLoader();
	}

	@Override
	public IdLoader<? extends Loader<BaseOrg>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(BaseOrg.class);
	}

}
