package com.n4systems.ws.resources;

import com.n4systems.model.EventType;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.exceptions.WsNotImplementedException;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.eventtype.WsEventType;
import com.n4systems.ws.model.eventtype.WsEventTypeConverter;

public class EventTypeResourceDefiner implements ResourceDefiner<EventType, WsEventType> {
	
	@Override
	public Class<WsEventType> getWsModelClass() {
		return WsEventType.class;
	}

	@Override
	public WsModelConverter<EventType, WsEventType> getResourceConverter() {
		return new WsEventTypeConverter();
	}

	@Override
	public ListLoader<EventType> getResourceListLoader(LoaderFactory loaderFactory) {
		throw new WsNotImplementedException();
	}

	@Override
	public IdLoader<FilteredIdLoader<EventType>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(EventType.class).setPostFetchFields("sections");
	}
	
}
