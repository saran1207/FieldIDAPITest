package com.n4systems.fieldid.ws.v1.resources.usergroup;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.user.UserGroup;

@Component
@Path("userGroup")
public class ApiUserGroupResource extends SetupDataResource<ApiUserGroup, UserGroup> {

	public ApiUserGroupResource() {
		super(UserGroup.class, true);
	}

	@Override
	protected ApiUserGroup convertEntityToApiModel(UserGroup usergroup) {
		ApiUserGroup apiUserGroup = new ApiUserGroup();
		apiUserGroup.setSid(usergroup.getId());
		apiUserGroup.setModified(usergroup.getModified());
		apiUserGroup.setActive(usergroup.isActive());
		apiUserGroup.setName(usergroup.getName());
		return apiUserGroup;
	}

}
