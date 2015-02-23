package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.ValueConverter;
import com.n4systems.fieldid.api.pub.model.Messages.UserGroupMessage;
import com.n4systems.fieldid.api.pub.model.Messages.UserGroupMessage.Builder;
import com.n4systems.model.user.UserGroup;

public class UserGroupToMessage extends TypeMapper<UserGroup, Builder> implements ValueConverter<UserGroup, UserGroupMessage> {

	public UserGroupToMessage() {
		super(TypeMapperBuilder.<UserGroup, Builder>newBuilder()
				.add(UserGroup::getPublicId, Builder::setId)
				.add(UserGroup::getName, Builder::setName)
				.add(UserGroup::getGroupId, Builder::setGroupId)
				.build());
	}

	@Override
	public UserGroupMessage convert(UserGroup group) {
		Builder builder = UserGroupMessage.newBuilder();
		map(group, builder);
		return builder.build();
	}
}
