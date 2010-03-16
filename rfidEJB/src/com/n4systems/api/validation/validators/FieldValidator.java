package com.n4systems.api.validation.validators;

import java.util.Map;

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
	public static final String ExternalOrgGlobalIdValidatorFail = "Could not find %s '%s'";
	public static final String ExternalOrgTypeValidatorFail = "%s must be either 'C' or 'D'";
	public static final String NotNullValidatorFail = "%s must not be blank";
	public static final String NamedFieldNotFoundValidatorFail = "Could not find an %s named '%s'";
	public static final String ProductTypeExistsValidatorFail = "Could not find a Product Type named '%s'";
	public static final String InputInfoFieldNotFoundValidatorFail = "The input field '%1$s' could not be found.  Please add the column 'I:%1$s'.";
	public static final String StaticOptionNotFoundValidatorFail = "The option '%s' could not be found for the field '%s'.";
	public static final String BlankInputOptionValidatorFail = "The input field '%s' must not be blank.";
	public static final String MissingRequiredProductAttributeValidatorFail = "The attribute '%s' is required.";
	public static final String InvalidDateValidatorFail = "The date '%s' in field '%s' is not valid.  Dates must be in the format '%s'.";
	public static final String OwnerResolutionValidatorFail = "Could not resolve an owner for organization '%s', customer '%s' and division '%s'.";
	
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext);
}
