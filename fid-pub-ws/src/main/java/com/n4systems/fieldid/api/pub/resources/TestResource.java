package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("photos")
public class TestResource extends FieldIdPersistenceService {

	@GET
	public String test() {
		return "Hello World";
	}

    @POST
    public String request() { return "Hello"; }

}
