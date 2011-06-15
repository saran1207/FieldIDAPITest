package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.GlobalIdExistsLoader;

public class ExternalUserGlobalIdValidator implements FieldValidator {
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object globalId, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		if (globalId == null) {
			// A null globalId just means it's an add
			return ValidationResult.pass();
		}
		
		GlobalIdExistsLoader idExistsLoader;
		
		idExistsLoader = new GlobalIdExistsLoader(filter, User.class);
		
		Boolean idExists = idExistsLoader.setGlobalId((String)globalId).load();
	
		if (idExists) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ExternalOrgGlobalIdValidatorFail, fieldName, globalId);
		}
		
	}

}
