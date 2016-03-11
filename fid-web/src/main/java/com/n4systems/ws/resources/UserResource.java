package com.n4systems.ws.resources;

import com.n4systems.model.user.User;
import com.n4systems.ws.model.setupdata.WsUser;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;


@Path("/User")
public class UserResource extends BaseResource<User, WsUser> {
	public UserResource(@Context UriInfo uriInfo) {
		super(uriInfo, new UserResourceDefiner());
	}
}
