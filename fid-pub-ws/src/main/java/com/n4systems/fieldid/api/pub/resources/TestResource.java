package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;

@Path("photos")
public class TestResource extends FieldIdPersistenceService {

	@Autowired
	private AuthService authService;

	@GET
	public String test() {
		return "Hello World";
	}

    @POST
    public String request() { return "Hello"; }

	@POST
	@Path("validate")
	public String validate(@QueryParam("consumer_key") String consumerKey,
						  @QueryParam("token_key") String tokenKey,
						  @QueryParam("nonce") String nonce,
						  @QueryParam("timestamp") Long timestamp) {

		if (!authService.validateRequest(consumerKey, tokenKey, nonce, timestamp)) {
			throw new ForbiddenException();
		}
		return "" + authService.exceddedRequestLimit(consumerKey, tokenKey, 3);
	}
}
