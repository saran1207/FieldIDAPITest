package com.n4systems.ws.resources;

import com.n4systems.model.ThingEventType;
import com.n4systems.ws.model.eventtype.WsEventType;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/EventType")
public class EventTypeResource extends BaseResource<ThingEventType, WsEventType> {

	public EventTypeResource(@Context UriInfo uriInfo) {
		super(uriInfo, new EventTypeResourceDefiner());
	}

}
