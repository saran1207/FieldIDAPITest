package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.exceptions.WsNotImplementedException;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.unitofmeasure.WsUnitOfMeasure;
import com.n4systems.ws.model.unitofmeasure.WsUnitOfMeasureConverter;

public class UnitOfMeasureResourceDefiner implements ResourceDefiner<UnitOfMeasure, WsUnitOfMeasure> {

	@Override
	public Class<WsUnitOfMeasure> getWsModelClass() {
		return WsUnitOfMeasure.class;
	}

	@Override
	public WsModelConverter<UnitOfMeasure, WsUnitOfMeasure> getResourceConverter() {
		return new WsUnitOfMeasureConverter();
	}

	@Override
	public Loader<List<UnitOfMeasure>> getResourceListLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createAllEntityListLoader(UnitOfMeasure.class);
	}

	@Override
	public IdLoader<? extends Loader<UnitOfMeasure>> getResourceIdLoader(LoaderFactory loaderFactory) {
		throw new WsNotImplementedException();
	}

}
