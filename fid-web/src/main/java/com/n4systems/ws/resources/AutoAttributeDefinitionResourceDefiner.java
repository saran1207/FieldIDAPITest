package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeDefinition;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeDefinitionConverter;

public class AutoAttributeDefinitionResourceDefiner implements ResourceDefiner<AutoAttributeDefinition, WsAutoAttributeDefinition> {

	@Override
	public WsModelConverter<AutoAttributeDefinition, WsAutoAttributeDefinition> getResourceConverter() {
		return new WsAutoAttributeDefinitionConverter();
	}

	@Override
	public Loader<List<LastModified>> getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(AutoAttributeDefinition.class);
	}

	@Override
	public IdLoader<? extends Loader<AutoAttributeDefinition>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(AutoAttributeDefinition.class).setPostFetchFields("criteria", "outputs");
	}

	@Override
	public Class<WsAutoAttributeDefinition> getWsModelClass() {
		return WsAutoAttributeDefinition.class;
	}

}
