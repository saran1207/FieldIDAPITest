package com.n4systems.fieldid.ws.v1.resources.user;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

@Component
@Path("users")
public class ApiUserResource extends SetupDataResource<ApiUser, User> {

	public ApiUserResource() {
		super(User.class);
	}

	@Override
	public ApiUser convertEntityToApiModel(User user) {
		ApiUser apiUser = new ApiUser();
		apiUser.setSid(user.getId());
		apiUser.setModified(user.getModified());
		apiUser.setActive(user.isActive());
		apiUser.setOwnerId(user.getOwner().getId());
		apiUser.setUserId(user.getUserID());
		apiUser.setName(user.getDisplayName());
		apiUser.setUserType(user.getUserType().name());
		apiUser.setHashPassword(user.getHashPassword());
		apiUser.setHashSecurityCardNumber(user.getHashSecurityCardNumber());
		apiUser.setCreateEventEnabled(Permissions.hasOneOf(user, Permissions.CreateEvent));
		apiUser.setEditEventEnabled(Permissions.hasOneOf(user, Permissions.EditEvent));
		apiUser.setIdentifyEnabled(Permissions.hasOneOf(user, Permissions.Tag));
		return apiUser;
	}

}
