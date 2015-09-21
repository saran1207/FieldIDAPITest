package com.n4systems.fieldid.ws.v2.resources.authentication;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v2.resources.setupdata.user.ApiUserResource;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/authenticate")
@Component
public class AuthenticationResource extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(AuthenticationResource.class);

	@Autowired protected UserService userService;
	@Autowired protected AdminUserService adminUserService;
	@Autowired protected ApiUserResource apiUserResource;
	@Autowired protected ApiTenantConverter apiTenantConverter;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ApiAuthResponse authenticate(
			@FormParam("tenant") String tenantName,
			@FormParam("user") String userId, 
			@FormParam("password") String password) {
		
		logger.info("Standard authentication for " + tenantName + " for " + userId);
		
		if (tenantName == null || userId == null || password == null) {
			throw new ForbiddenException();
		}

		User user = userService.authenticateUserByPassword(tenantName, userId, password);
		if (user == null) {
			user = adminUserService.attemptSudoAuthentication(tenantName, userId, password);
		}
		return authenticateUser(user);
	}
	
	@POST
	@Path("passcode")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ApiAuthResponse authenticate(
			@FormParam("tenant") String tenantName,
			@FormParam("passcode") String passcode) {
		
		logger.info("Passcode authentication for " + tenantName);
		
		if (tenantName == null || passcode == null) {
			throw new ForbiddenException();
		}
		
		User user = userService.authenticateUserBySecurityCard(tenantName, passcode);
		return authenticateUser(user);
	}
	
	private ApiAuthResponse authenticateUser(User user) {
		if (user == null) {
			throw new ForbiddenException();
		}
		
		securityContext.setUserSecurityFilter(new UserSecurityFilter(user));
		securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(user.getTenant().getId()));

		ApiAuthResponse authResponse = new ApiAuthResponse();
		authResponse.setUser(apiUserResource.convertEntityToApiModel(user));
		authResponse.setTenant(apiTenantConverter.convertEntityToApiModel(user.getOwner().getPrimaryOrg()));
		return authResponse;
	}
	
}
