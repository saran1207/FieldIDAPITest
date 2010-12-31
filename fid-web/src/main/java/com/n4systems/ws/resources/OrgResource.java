package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.ws.model.org.WsOrg;

@Path("/Org")
public class OrgResource extends BaseResource<BaseOrg, WsOrg> {

	public OrgResource(@Context UriInfo uriInfo) {
		super(uriInfo, new OrgResourceDefiner());
	}

}
