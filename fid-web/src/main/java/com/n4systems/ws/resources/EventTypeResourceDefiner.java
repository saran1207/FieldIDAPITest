package com.n4systems.ws.resources;


import com.n4systems.model.EventType;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.ws.model.WsModelConverter;
import com.n4systems.ws.model.eventtype.WsEventType;
import com.n4systems.ws.model.eventtype.WsEventTypeConverter;

public class EventTypeResourceDefiner implements ResourceDefiner<EventType, WsEventType> {

	@Override
	public WsModelConverter<EventType, WsEventType> getResourceConverter() {
		return new WsEventTypeConverter();
	}

	@Override
	public LastModifiedListLoader getLastModifiedLoader(LoaderFactory loaderFactory) {
		Long tenantId = loaderFactory.getSecurityFilter().getTenantId();
		return LastModifiedListLoader.create(new TenantOnlySecurityFilter(tenantId), EventType.class);
	}

	@Override
	public IdLoader<FilteredIdLoader<EventType>> getResourceIdLoader(LoaderFactory loaderFactory) {
		return loaderFactory.createFilteredIdLoader(EventType.class).setPostFetchFields("eventForm.sections", "infoFieldNames");
	}

	@Override
	public Class<WsEventType> getWsModelClass() {
		return WsEventType.class;
	}
	
}
