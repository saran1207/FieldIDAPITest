package com.n4systems.api.validation.validators;

import java.util.Map;

import javax.persistence.NonUniqueResultException;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.exporting.beanutils.OwnerSerializationHandler;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.StringUtils;

public class OwnerExistsValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		if (fieldValue == null) {
			return ValidationResult.pass();
		}
		
		String[] orgNames = (String[])fieldValue;
		String organization = orgNames[OwnerSerializationHandler.OWNER_ORGANIZATION]; 
		String customer = orgNames[OwnerSerializationHandler.OWNER_CUSTOMER];
		String division = orgNames[OwnerSerializationHandler.OWNER_DIVISION];
		
		OrgByNameLoader orgExistsLoader = createOrgExistsLoader(filter);
		orgExistsLoader.setOrganizationName(organization);
		orgExistsLoader.setCustomerName(customer);
		orgExistsLoader.setDivision(division);
		
		BaseOrg org = null;
		try {
			org = orgExistsLoader.load();
		} catch (NonUniqueResultException e) {
			return ValidationResult.fail(NonUniqueOwnerValidatorFail, StringUtils.stringOrEmpty(organization), StringUtils.stringOrEmpty(customer), StringUtils.stringOrEmpty(division));
		} catch (RuntimeException e) {
			// ignore anything else
		}
		
		if (org != null) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(OwnerResolutionValidatorFail, StringUtils.stringOrEmpty(organization), StringUtils.stringOrEmpty(customer), StringUtils.stringOrEmpty(division));
		}
	}

	protected OrgByNameLoader createOrgExistsLoader(SecurityFilter filter) {
		return new OrgByNameLoader(filter);
	}
}
