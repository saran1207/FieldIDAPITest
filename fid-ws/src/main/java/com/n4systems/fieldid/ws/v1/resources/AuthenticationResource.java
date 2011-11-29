package com.n4systems.fieldid.ws.v1.resources;

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

import com.mysql.jdbc.StringUtils;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.fieldid.ws.v1.models.ApiUser;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

@Path("/authenticate.json")
@Component
@Scope("request")
public class AuthenticationResource extends FieldIdPersistenceService {

	@Autowired
	private UserService userService;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ApiUser authenticate(
			@FormParam("companyId") String tenantName,
			@FormParam("userId") String userId, 
			@FormParam("password") String password) {
		
		if (StringUtils.isNullOrEmpty(tenantName) || StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(password)) {
			throw new ForbiddenException();
		}

		User user = userService.authenticateUserByPassword(tenantName, userId, password);
		if (user == null) {
			throw new ForbiddenException();
		}
		
		ApiUser apiUser = convertUserToApiUser(user);
		return apiUser;
	}
	
	private ApiUser convertUserToApiUser(User user) {
		ApiUser apiUser = new ApiUser();
		apiUser.setId(user.getId());
		apiUser.setActive(user.isActive());
		apiUser.setOwnerId(user.getOwner().getId());
		apiUser.setUserId(user.getUserID());
		apiUser.setAuthKey(user.getAuthKey());
		apiUser.setName(user.getDisplayName());
		apiUser.setUserType(user.getUserType().name());
		apiUser.setHashPassword(user.getHashPassword());
		apiUser.setHashSecurityCardNumber(user.getHashSecurityCardNumber());
		apiUser.getPermissions().setCreateEvent(Permissions.hasOneOf(user, Permissions.CreateEvent));
		apiUser.getPermissions().setEditEvent(Permissions.hasOneOf(user, Permissions.EditEvent));
		apiUser.getPermissions().setIdentify(Permissions.hasOneOf(user, Permissions.Tag));
		return apiUser;
	}
	
}
