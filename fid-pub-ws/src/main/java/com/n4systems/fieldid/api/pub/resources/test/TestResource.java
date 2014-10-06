package com.n4systems.fieldid.api.pub.resources.test;

import com.n4systems.fieldid.api.pub.resources.ListResponse;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("photos")
@Scope("request")
public class TestResource extends FieldIdPersistenceService {

	@Autowired
	private AuthService authService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ListResponse test() {
		TestModel model = new TestModel();
		model.setId("idstring");
		model.setName("Hello World");

		List<TestModel> models = new ArrayList<>();
		models.add(model);

		return new ListResponse<TestModel>()
				.setPage(0)
				.setPageSize(10)
				.setTotal(1)
				.setItems(models);
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
}
