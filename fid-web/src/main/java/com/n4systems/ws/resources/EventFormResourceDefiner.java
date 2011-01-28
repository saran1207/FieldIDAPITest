package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.EventForm;
import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.eventtype.WsEventForm;
import com.n4systems.ws.model.eventtype.WsEventFormConverter;

public class EventFormResourceDefiner implements ResourceDefiner<EventForm, WsEventForm> {

	@Override
	public WsModelConverter<EventForm, WsEventForm> getResourceConverter() {
		return new WsEventFormConverter();
	}

	@Override
	public Loader<List<LastModified>> getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(EventForm.class);
	}

	@Override
	public IdLoader<? extends Loader<EventForm>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(EventForm.class).setPostFetchFields("sections");
	}

	@Override
	public Class<WsEventForm> getWsModelClass() {
		return WsEventForm.class;
	}

}