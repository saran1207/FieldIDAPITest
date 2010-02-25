package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.security.SecurityFilter;

public interface FieldValidator {
	/*
	 * For now, we'll put these response messages in here so at least they're in one place.  
	 * At some point they should be moved out and possibly shared with web language files
	 * TODO: Move the FieldValidator messages out to a language file
	 */
	public static final String EmailValidatorFail = "%s '%s' is not a valid Email address";
	public static final String ExternalOrgCodeUniqueValidatorFail = "%s '%s' already exists in the system";
	public static final String ExternalOrgGlobalIdValidatorFail = "Could not find %s '%s'";
	public static final String ExternalOrgTypeValidatorFail = "%s must be either 'C' or 'D'";
	public static final String NotNullValidatorFail = "%s must not be blank";
	public static final String ParentOrgResolutionValidatorFail = "Could not find an %s named '%s'";
	public static final String ProductTypeExistsValidatorFail = "Could not find a Product Type named '%s'";

	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter);
}
