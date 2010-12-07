package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.EventType;
import com.n4systems.ws.model.eventtype.WsEventType;

@Path("/EventType")
public class EventTypeResource extends BaseResource<EventType, WsEventType> {

	public EventTypeResource(@Context UriInfo uriInfo) {
		super(uriInfo, new EventTypeResourceDefiner());
	}

}
