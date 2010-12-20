package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeCriteria;

@Path("/AutoAttribute")
public class AutoAttributeResource extends BaseResource<AutoAttributeCriteria, WsAutoAttributeCriteria> {

	public AutoAttributeResource(@Context UriInfo uriInfo) {
		super(uriInfo, new AutoAttributeResourceDefiner());
	}
	
}
