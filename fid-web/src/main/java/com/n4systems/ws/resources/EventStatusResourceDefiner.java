package com.n4systems.ws.resources;

import com.n4systems.model.EventStatus;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.eventtype.WsEventStatus;
import com.n4systems.ws.model.eventtype.WsEventStatusConverter;

public class EventStatusResourceDefiner implements ResourceDefiner<EventStatus, WsEventStatus> {
	
	@Override
	public WsModelConverter<EventStatus, WsEventStatus> getResourceConverter() {
		return new WsEventStatusConverter();
	}

	@Override
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(EventStatus.class);
	}

	@Override
	public IdLoader<? extends Loader<EventStatus>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(EventStatus.class);
	}

	@Override
	public Class<WsEventStatus> getWsModelClass() {
		return WsEventStatus.class;
	}
}
