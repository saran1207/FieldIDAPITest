package com.n4systems.ws.resources;


import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.unitofmeasure.WsUnitOfMeasure;
import com.n4systems.ws.model.unitofmeasure.WsUnitOfMeasureConverter;

public class UnitOfMeasureResourceDefiner implements ResourceDefiner<UnitOfMeasure, WsUnitOfMeasure> {

	@Override
	public WsModelConverter<UnitOfMeasure, WsUnitOfMeasure> getResourceConverter() {
		return new WsUnitOfMeasureConverter();
	}

	@Override
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(UnitOfMeasure.class);
	}

	@Override
	public IdLoader<? extends Loader<UnitOfMeasure>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(UnitOfMeasure.class);
	}

	@Override
	public Class<WsUnitOfMeasure> getWsModelClass() {
		return WsUnitOfMeasure.class;
	}

}
