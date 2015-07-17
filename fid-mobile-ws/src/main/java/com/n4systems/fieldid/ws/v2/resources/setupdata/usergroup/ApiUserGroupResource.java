package com.n4systems.fieldid.ws.v2.resources.setupdata.usergroup;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.user.UserGroup;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("userGroup")
public class ApiUserGroupResource extends SetupDataResourceReadOnly<ApiUserGroup, UserGroup> {

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
