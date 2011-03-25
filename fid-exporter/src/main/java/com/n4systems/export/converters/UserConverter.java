package com.n4systems.export.converters;

import com.n4systems.model.user.User;

public class UserConverter extends ExportValueConverter<User> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(User.class);
	}

	@Override
	public String convertValue(User obj) {
		return obj.getDisplayName();
	}

}
