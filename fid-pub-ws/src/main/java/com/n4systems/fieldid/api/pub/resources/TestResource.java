package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Tenant;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class TestResource extends FieldIdPersistenceService {

	@GET
	public String test() {
		Tenant t = persistenceService.findNonSecure(Tenant.class, 15511493L);
		return "Hello World: " + t.getName() + " V1";
	}

}
