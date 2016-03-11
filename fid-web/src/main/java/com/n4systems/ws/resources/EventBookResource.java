package com.n4systems.ws.resources;

import com.n4systems.model.EventBook;
import com.n4systems.ws.model.eventbook.WsEventBook;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/EventBook")
public class EventBookResource extends BaseResource<EventBook, WsEventBook> {

	public EventBookResource(@Context UriInfo uriInfo) {
		super(uriInfo, new EventBookResourceDefiner());
	}

}
