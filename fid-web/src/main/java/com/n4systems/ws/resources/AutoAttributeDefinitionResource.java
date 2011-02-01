package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeDefinition;

@Path("/AutoAttributeDefinition")
public class AutoAttributeDefinitionResource extends BaseResource<AutoAttributeDefinition, WsAutoAttributeDefinition> {
	
	public AutoAttributeDefinitionResource(@Context UriInfo uriInfo) {
		super(uriInfo, new AutoAttributeDefinitionResourceDefiner());
	}
	
}
