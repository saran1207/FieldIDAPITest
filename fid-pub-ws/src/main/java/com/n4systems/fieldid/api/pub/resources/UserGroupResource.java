package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserGroupToMessage;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.UserGroupMessage;
import com.n4systems.fieldid.api.pub.model.Messages.UserGroupMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.user.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path("userGroups")
@Component
public class UserGroupResource extends CrudResource<UserGroup, UserGroupMessage, Builder> {

	@Autowired
	private UserGroupService userGroupService;

	public UserGroupResource() {
		super(Messages.userGroups);
	}

	@Override
	protected CrudService<UserGroup> crudService() {
		return userGroupService;
	}

	@Override
	protected UserGroup createModel(UserGroupMessage message) {
		return new UserGroup();
	}

	@Override
	protected Builder createMessageBuilder(UserGroup model) {
		return UserGroupMessage.newBuilder();
	}

	@Override
	protected Mapper<UserGroup, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<UserGroup, Builder> mapperBuilder) {
		return new UserGroupToMessage();
	}

	@Override
	protected Mapper<UserGroupMessage, UserGroup> createMessageToModelMapper(TypeMapperBuilder<UserGroupMessage, UserGroup> mapperBuilder) {
		return mapperBuilder
				.add(UserGroupMessage::getName, UserGroup::setName)
				.add(UserGroupMessage::getGroupId, UserGroup::setGroupId)
				.build();
	}
}
