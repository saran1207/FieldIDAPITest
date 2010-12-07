package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.eventtype.EventTypeFormVersion;
import com.n4systems.ws.model.eventtype.WsEventTypeFormVersion;

@Path("/EventTypeFormVersion")
public class EventTypeFormVersionResource extends BaseResource<EventTypeFormVersion, WsEventTypeFormVersion> {
	
	public EventTypeFormVersionResource(@Context UriInfo uriInfo) {
		super(uriInfo, new EventTypeFormVersionResourceDefiner());
	}
	
}
