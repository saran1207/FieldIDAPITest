package com.n4systems.ws.resources;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.n4systems.ws.model.eventtype.WsEventType;
import com.n4systems.ws.model.eventtype.WsEventTypeFormVersion;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
	private static final Class<?>[] types = { 
		WsEventTypeFormVersion.class,
		WsEventType.class
	};
	
	private final JAXBContext context;
	
	public JAXBContextResolver() throws Exception {
		context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
	}

	public JAXBContext getContext(Class<?> objectType) {
		for (Class<?> type: types) {
			if (type == objectType) {
				return context;
			}
		}
		return null;
	}

}
