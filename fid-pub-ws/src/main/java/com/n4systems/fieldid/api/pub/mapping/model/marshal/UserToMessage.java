package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.model.user.User;

public class UserToMessage<T> extends TypeMapper<User, T> {

	public UserToMessage(SetterReference<T, String> idSetter, SetterReference<T, String> nameSetter) {
		super(TypeMapperBuilder.<User, T>newBuilder()
				.add(User::getPublicId, idSetter, false)
				.add(User::getFullName, nameSetter, false)
				.build());
	}
}
