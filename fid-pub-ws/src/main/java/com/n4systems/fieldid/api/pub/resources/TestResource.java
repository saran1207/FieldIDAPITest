package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.serialization.Messages;
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
		return "" + authService.exceededRequestLimit(consumerKey, tokenKey, 3);
	}

	@GET
	@Path("clear")
	public void truncate() {
		authService.clearRequestLog();
	}

    @POST
    @Path("createEntity")
    @Produces({"application/x-protobuf64", "application/x-protobuf64"})
//    @Produces("application/x-protobuf64")
    public Messages.TestEntityCreateResponse createEntity(Messages.TestEntityCreateMessage msg)
    {
        return Messages.TestEntityCreateResponse.newBuilder().setEntityCode(msg.getEntityCode()).setStatus(200).build();
    }
}
