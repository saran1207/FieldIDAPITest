package com.n4systems.fieldid.ws.v1.resources.authentication;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.model.user.User;

@Path("/authenticate")
@Component
@Scope("request")
public class AuthenticationResource extends FieldIdPersistenceService {

	@Autowired private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	@Transactional(readOnly = true)
	public String authenticate(
			@FormParam("tenant") String tenantName,
			@FormParam("user") String userId, 
			@FormParam("password") String password) {
		
		if (tenantName == null || userId == null || password == null) {
			throw new ForbiddenException();
		}

		User user = userService.authenticateUserByPassword(tenantName, userId, password);
		if (user == null) {
			throw new ForbiddenException();
		}
		
		return user.getAuthKey();
	}
	
}
