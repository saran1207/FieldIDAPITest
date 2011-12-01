package com.n4systems.ws.resources;


import com.n4systems.model.EventBook;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.eventbook.WsEventBook;
import com.n4systems.ws.model.eventbook.WsEventBookConverter;

public class EventBookResourceDefiner implements ResourceDefiner<EventBook, WsEventBook> {

	@Override
	public WsModelConverter<EventBook, WsEventBook> getResourceConverter() {
		return new WsEventBookConverter();
	}

	@Override
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createLastModifiedListLoader(EventBook.class);
	}

	@Override
	public IdLoader<? extends Loader<EventBook>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(EventBook.class);
	}

	@Override
	public Class<WsEventBook> getWsModelClass() {
		return WsEventBook.class;
	}

}
