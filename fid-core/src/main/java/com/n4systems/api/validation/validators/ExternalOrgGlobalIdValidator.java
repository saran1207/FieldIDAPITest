package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.api.Exportable;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdExistsLoader;

public class ExternalOrgGlobalIdValidator implements FieldValidator {
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object globalId, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		if (globalId == null) {
			// A null globalId just means it's an add
			return ValidationResult.pass();
		}
		
		GlobalIdExistsLoader idExistsLoader;
		
		FullExternalOrgView orgView = (FullExternalOrgView)view;
		if (orgView.isCustomer()) {
			idExistsLoader = getGlobalIdExistsLoader(CustomerOrg.class, filter);
		
		} else if (orgView.isDivision()) {
			idExistsLoader = getGlobalIdExistsLoader(DivisionOrg.class, filter);
		
		} else {
			/*
			 * if the orgView was not a customer or division, then we'll let this one pass.
			 * The work of validating the org type should be left to ExternalOrgTypeValidator
			 */
			return ValidationResult.pass();
		}
		
		Boolean idExists = idExistsLoader.setGlobalId((String)globalId).load();
	
		if (idExists) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ExternalOrgGlobalIdValidatorFail, fieldName, globalId);
		}
		
	}

	protected GlobalIdExistsLoader getGlobalIdExistsLoader(Class<? extends Exportable> clazz, SecurityFilter filter) {
		return new GlobalIdExistsLoader(filter, clazz);
	}
}
