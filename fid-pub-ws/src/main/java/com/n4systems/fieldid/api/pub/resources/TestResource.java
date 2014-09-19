package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class TestResource extends FieldIdPersistenceService {

	@GET
	public String test() {
		return "Hello World";
	}

}
