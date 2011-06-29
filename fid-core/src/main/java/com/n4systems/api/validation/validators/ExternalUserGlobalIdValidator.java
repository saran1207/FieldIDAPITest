package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByUserIdLoader;
import com.n4systems.persistence.loaders.GlobalIdExistsLoader;

public class ExternalUserGlobalIdValidator implements FieldValidator {
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object globalId, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		// this is a BUH that...
		// 1: assumes view is a UserView (yucchy casting). 
		// 2: takes a peek at two fields somewhat independently.  field validators should really just deal with one at a time. 
		// 3: might cause performance problems because each import validation *will* do a database call. (doing one large sql statement would be better).
		//     i.e. if usersExist{a,b,c,d,e,f} then canAdd=false;  instead of checking a, then b, then c etc...
		
		UserView userView = (UserView) view;
		if (globalId == null) {
			if (userView.getUserID()==null) { 
				return ValidationResult.fail(ExternalUserIdMissingValidatorFail, userView.getFirstName(), userView.getLastName());
			}
			// A null globalId just means it's an add
			// so we need to check that the new user isn't trying to use an existing ID.  			
			UserByUserIdLoader userLoader = createUserByUserIdLoader(filter, userView).setUserID(userView.getUserID());
			return userLoader.load()==null ? 
						ValidationResult.pass() : 
						ValidationResult.fail(ExternalUserExistsValidationFail, userView.getUserID());
		} else { 				
			GlobalIdExistsLoader idExistsLoader = createGlobalIdExistsLoader(filter).setGlobalId((String)globalId);		
			return idExistsLoader.load() ? 
					ValidationResult.pass() : 
					ValidationResult.fail(ExternalUserGlobalIdValidatorFail, userView.getFirstName(), userView.getLastName(), globalId);
		}
		
	}

	GlobalIdExistsLoader createGlobalIdExistsLoader(SecurityFilter filter) {
		return new GlobalIdExistsLoader(filter, User.class);
	}

	UserByUserIdLoader createUserByUserIdLoader(SecurityFilter filter, UserView userView) {
		return new UserByUserIdLoader(filter);
	}

}
