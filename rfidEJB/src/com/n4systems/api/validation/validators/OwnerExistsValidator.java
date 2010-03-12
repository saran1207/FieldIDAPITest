package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.orgs.OrgWithNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class OwnerExistsValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String orgName = (String)fieldValue;
		OrgWithNameExistsLoader orgExistsLoader = createOrgExistsLoader(filter).setName(orgName);
		
		if (orgExistsLoader.load()) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(NamedFieldNotFoundValidatorFail, fieldName, orgName);
		}
	}

	protected OrgWithNameExistsLoader createOrgExistsLoader(SecurityFilter filter) {
		return new OrgWithNameExistsLoader(filter);
	}
}
