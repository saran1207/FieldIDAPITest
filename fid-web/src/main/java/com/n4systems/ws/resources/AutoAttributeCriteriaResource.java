package com.n4systems.ws.resources;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeCriteria;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/AutoAttributeCriteria")
public class AutoAttributeCriteriaResource extends BaseResource<AutoAttributeCriteria, WsAutoAttributeCriteria> {

	public AutoAttributeCriteriaResource(@Context UriInfo uriInfo) {
		super(uriInfo, new AutoAttributeCriteriaResourceDefiner());
	}
	
}
