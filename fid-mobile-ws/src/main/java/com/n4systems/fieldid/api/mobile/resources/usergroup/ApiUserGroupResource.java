package com.n4systems.fieldid.api.mobile.resources.usergroup;

import com.n4systems.fieldid.api.mobile.resources.SetupDataResource;
import com.n4systems.model.user.UserGroup;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

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
