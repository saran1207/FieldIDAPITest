package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.orgs.internal.InternalOrgWithNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class ParentOrgResolutionValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		FullExternalOrgView orgView = (FullExternalOrgView)view;
		if (!orgView.isCustomer()) {
			// this validation only applies to the customer
			// as division get their parent customer set for them
			return ValidationResult.pass();
		}
		
		// this guy also has to act as a not null validator since it's only not null for customers
		if (orgView.getParentOrg() == null) {
			return ValidationResult.fail(NotNullValidatorFail, fieldName);
		}
		
		
		boolean orgExists = createOrgExistsLoader(filter).setName(orgView.getParentOrg()).load();
	
		if (orgExists) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ParentOrgResolutionValidatorFail, fieldName, orgView.getParentOrg());
		}
	}
	
	protected InternalOrgWithNameExistsLoader createOrgExistsLoader(SecurityFilter filter) {
		return new InternalOrgWithNameExistsLoader(filter);
	}

}
