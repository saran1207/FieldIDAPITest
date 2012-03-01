package com.n4systems.fieldid.ws.v1.resources.authentication;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUser;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUserResource;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;

@Path("/authenticate")
@Component
public class AuthenticationResource extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(AuthenticationResource.class);

	@Autowired protected UserService userService;
	@Autowired protected ApiUserResource apiUserResource;
	@Autowired protected SecurityContext securityContext;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ApiUser authenticate(
			@FormParam("tenant") String tenantName,
			@FormParam("user") String userId, 
			@FormParam("password") String password) {
		
		logger.info("Standard authentication for " + tenantName + " for " + userId);
		
		if (tenantName == null || userId == null || password == null) {
			throw new ForbiddenException();
		}

		User user = userService.authenticateUserByPassword(tenantName, userId, password);
		return authenticateUser(user);
	}
	
	@POST
	@Path("passcode")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ApiUser authenticate(
			@FormParam("tenant") String tenantName,
			@FormParam("passcode") String passcode) {
		
		logger.info("Passcode authentication for " + tenantName);
		
		if (tenantName == null || passcode == null) {
			throw new ForbiddenException();
		}
		
		User user = userService.authenticateUserBySecurityCard(tenantName, passcode);
		return authenticateUser(user);
	}
	
	private ApiUser authenticateUser(User user) {
		if (user == null) {
			throw new ForbiddenException();
		}
		
		securityContext.setUserSecurityFilter(new UserSecurityFilter(user));
		securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(user.getTenant().getId()));
		
		ApiUser apiUser = apiUserResource.convertEntityToApiModel(user);
		return apiUser;
	}
	
}
