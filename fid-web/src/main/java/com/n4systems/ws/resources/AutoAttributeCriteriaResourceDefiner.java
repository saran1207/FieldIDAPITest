package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeCriteria;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeCriteriaConverter;

public class AutoAttributeCriteriaResourceDefiner implements ResourceDefiner<AutoAttributeCriteria, WsAutoAttributeCriteria> {

	@Override
	public WsModelConverter<AutoAttributeCriteria, WsAutoAttributeCriteria> getResourceConverter() {
		return new WsAutoAttributeCriteriaConverter();
	}

	@Override
	public Loader<List<LastModified>> getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(AutoAttributeCriteria.class);
	}

	@Override
	public IdLoader<? extends Loader<AutoAttributeCriteria>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(AutoAttributeCriteria.class).setPostFetchFields("inputs", "outputs");
	}

	@Override
	public Class<WsAutoAttributeCriteria> getWsModelClass() {
		return WsAutoAttributeCriteria.class;
	}

}
