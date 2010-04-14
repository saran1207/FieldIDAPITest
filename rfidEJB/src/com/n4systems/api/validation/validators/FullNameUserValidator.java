package com.n4systems.api.validation.validators;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rfid.ejb.entity.UserBean;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.UserByFullNameLoader;

public class FullNameUserValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String userName = (String)fieldValue;
		
		UserByFullNameLoader userLoader = createUserByFullNameLoader(filter);		
		userLoader.setFullName(userName);
		
		List<UserBean> users = userLoader.load();
		if (users.isEmpty()) {
			return ValidationResult.fail(NoUserFoundValidationFail, userName, fieldName);
		} else if (users.size() > 1) {
			return ValidationResult.fail(MultipleUserFoundValidationFail, userName, formatUserNameList(users), fieldName);
		} else {
			return ValidationResult.pass();
		}
	}

	private String formatUserNameList(List<UserBean> users) {
		StringBuilder userNameList = new StringBuilder();
		
		Iterator<UserBean> userIterator = users.iterator();
		
		userNameList.append('\'').append(userIterator.next().getFullName()).append('\'');
		while (userIterator.hasNext()) {
			userNameList.append(", '");
			userNameList.append(userIterator.next().getFullName());
			userNameList.append('\'');
		}
		
		return userNameList.toString();
	}

	protected UserByFullNameLoader createUserByFullNameLoader(SecurityFilter filter) {
		return new UserByFullNameLoader(filter);
	}
	
}
