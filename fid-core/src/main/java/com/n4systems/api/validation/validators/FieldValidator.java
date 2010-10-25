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
	public static final String ProductViewStringLengthValidatorFail = "%s cannot exceed %d characters";
	public static final String NamedFieldNotFoundValidatorFail = "Could not find an %s named '%s'";
	public static final String ProductTypeExistsValidatorFail = "Could not find an Asset Type named '%s'";
	public static final String InputInfoFieldNotFoundValidatorFail = "The input field '%1$s' could not be found.  Please add the column 'I:%1$s'.";
	public static final String StaticOptionNotFoundValidatorFail = "The option '%s' could not be found for the field '%s'.";
	public static final String BlankInputOptionValidatorFail = "The input field '%s' must not be blank.";
	public static final String MissingRequiredProductAttributeValidatorFail = "The attribute '%s' is required.";
	public static final String InvalidDateValidatorFail = "The date '%s' in field '%s' is not valid.  Please ensure the date is in the correct format.";
	public static final String OwnerResolutionValidatorFail = "Could not find an owner for organization '%s', customer '%s' and division '%s'.";
	public static final String NonUniqueOwnerValidatorFail = "Found multiple owners for organization '%s', customer '%s' and division '%s'.";
	public static final String InspectionStatusValidatorFail = "'%s' is not a valid result.  Results may be 'Pass', 'Fail', or 'N/A'";
	public static final String YNValidatorFail = "'%s' is not allowed for the field '%s'.  Valid values are 'Y' or 'N'.";
	public static final String NoUserFoundValidationFail = "Could not find a user with the name '%s' for the field '%s'.";
	public static final String MultipleUserFoundValidationFail = "Multiple users have been found matching the name '%s'.  The matching users are: '%s'.";
	public static final String NoProductFoundValidationFail = "Could not find an asset matching the identifier '%s' for the field '%s'.";
	public static final String MultipleProductFoundValidationFail = "Multiple assets have been found matching the identifier '%s' for the field '%s'.";
	public static final String AssociatedInspectionTypeValidationFail = "The inspection type '%s' is not allowed for the asset type '%s'.";
	
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext);
}
