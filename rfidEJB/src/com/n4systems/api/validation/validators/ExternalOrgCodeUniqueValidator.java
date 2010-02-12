package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.orgs.external.ExternalOrgCodeExistsLoader;
import com.n4systems.model.security.SecurityFilter;

public class ExternalOrgCodeUniqueValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object globalId, V view, String fieldName, SecurityFilter filter) {
		ExternalOrgCodeExistsLoader codeExistsLoader;
		
		FullExternalOrgView orgView = (FullExternalOrgView)view;
		if (orgView.isCustomer()) {
			codeExistsLoader = getCodeExistsLoader(CustomerOrg.class, filter);
		
		} else if (orgView.isDivision()) {
			codeExistsLoader = getCodeExistsLoader(DivisionOrg.class, filter);
		
		} else {
			/*
			 * if the orgView was not a customer or division, then we'll let this one pass.
			 * The work of validating the org type should be left to ExternalOrgTypeValidator
			 */
			return ValidationResult.pass();
		}
		
		Boolean codeExists = codeExistsLoader.setFilterOutGlobalId(orgView.getGlobalId()).setCode(orgView.getCode()).load();
	
		if (!codeExists) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ExternalOrgCodeUniqueValidatorFail, fieldName, orgView.getCode());
		}
	}

	protected ExternalOrgCodeExistsLoader getCodeExistsLoader(Class<? extends ExternalOrg> clazz, SecurityFilter filter) {
		return new ExternalOrgCodeExistsLoader(filter, clazz);
	}

}
