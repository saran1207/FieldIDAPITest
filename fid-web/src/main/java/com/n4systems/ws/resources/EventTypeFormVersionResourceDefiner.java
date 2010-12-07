package com.n4systems.ws.resources;

import com.n4systems.model.eventtype.EventTypeFormVersion;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.exceptions.WsNotImplementedException;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.eventtype.WsEventTypeFormVersion;
import com.n4systems.ws.model.eventtype.WsEventTypeFormVersionConverter;

public class EventTypeFormVersionResourceDefiner implements ResourceDefiner<EventTypeFormVersion, WsEventTypeFormVersion> {
	
	@Override
	public Class<WsEventTypeFormVersion> getWsModelClass() {
		return WsEventTypeFormVersion.class;
	}

	@Override
	public WsModelConverter<EventTypeFormVersion, WsEventTypeFormVersion> getResourceConverter() {
		return new WsEventTypeFormVersionConverter();
	}

	@Override
	public ListLoader<EventTypeFormVersion> getResourceListLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createEventTypeFormVersionLoader();
	}

	@Override
	public IdLoader<Loader<EventTypeFormVersion>> getResourceIdLoader(LoaderFactory loaderFactory) {
		throw new WsNotImplementedException();
	}
}
