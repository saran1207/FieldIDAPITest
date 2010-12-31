package com.n4systems.ws.resources;

import java.util.List;

import com.n4systems.model.EventBook;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.eventbook.WsEventBook;
import com.n4systems.ws.model.eventbook.WsEventBookConverter;

public class EventBookResourceDefiner implements ResourceDefiner<EventBook, WsEventBook> {

	@Override
	public Class<WsEventBook> getWsModelClass() {
		return WsEventBook.class;
	}

	@Override
	public WsModelConverter<EventBook, WsEventBook> getResourceConverter() {
		return new WsEventBookConverter();
	}

	@Override
	public Loader<List<EventBook>> getResourceListLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createAllEventBookListLoader();
	}

	@Override
	public IdLoader<? extends Loader<EventBook>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(EventBook.class);
	}

}
