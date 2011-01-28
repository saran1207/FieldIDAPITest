package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.EventForm;
import com.n4systems.ws.model.eventtype.WsEventForm;

@Path("/EventForm")
public class EventFormResource extends BaseResource<EventForm, WsEventForm> {

	public EventFormResource(@Context UriInfo uriInfo) {
		super(uriInfo, new EventFormResourceDefiner());
	}

}
