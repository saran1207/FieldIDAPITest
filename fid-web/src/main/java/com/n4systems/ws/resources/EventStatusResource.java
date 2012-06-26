package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.EventStatus;
import com.n4systems.ws.model.eventtype.WsEventStatus;

@Path("/EventStatus")
public class EventStatusResource extends BaseResource<EventStatus, WsEventStatus> {
	public EventStatusResource(@Context UriInfo uriInfo) {
		super(uriInfo, new EventStatusResourceDefiner());
	}
}
