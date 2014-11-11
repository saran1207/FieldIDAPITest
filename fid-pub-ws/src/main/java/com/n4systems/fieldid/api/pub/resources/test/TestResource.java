package com.n4systems.fieldid.api.pub.resources.test;

import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

@Path("test")
@Component
public class TestResource extends FieldIdPersistenceService {

	@Autowired
	private AuthService authService;

	@GET
	@Produces("application/json")
	public Messages.OwnerMessage test() {
		return Messages.OwnerMessage.newBuilder().build();
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

    @POST
    @Consumes("application/json")
    @Path("test_object")
    public String test_object(Messages.AssetMessage asset) {
        return asset.getId();
    }



	@GET
	@Path("clear")
	public void truncate() {
		authService.clearRequestLog();
	}
}
