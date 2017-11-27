package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;

import java.util.Map;

public interface FieldValidator {
	/*
	 * For now, we'll put these response messages in here so at least they're in one place.  
	 * At some point they should be moved out and possibly shared with web language files
	 * TODO: Move the FieldValidator messages out to a language file
	 */
	public static final String CriteriaValidatorUnitOfMeasureFail = "Only two '|' delimited values are allowed when specifying unit of measure values --> ";
	public static final String EmailValidatorFail = "%s '%s' is not a valid Email address";
	public static final String ExternalUserIdMissingValidatorFail = "You must specify the userid for user %s %s";
	public static final String ExternalUserExistsValidationFail = "A user with id %s already exists";
	public static final String ExternalUserGlobalIdValidatorFail = "Could not find user %s %s with global id %s";
	public static final String ExternalOrgGlobalIdValidatorFail = "Could not find %s '%s'";
	public static final String ExternalOrgTypeValidatorFail = "%s must be either 'C' or 'D'";
	public static final String NotNullValidatorFail = "%s must not be blank";
	public static final String StringLengthValidatorFail = "%s cannot exceed %d characters";
	public static final String NamedFieldNotFoundValidatorFail = "Could not find an %s named '%s'";
    public static final String UserGroupNotFoundValidatorFail = "Could not find a User Group named '%s'";
	public static final String AssetTypeExistsValidatorFail = "Could not find an Asset Type named '%s'";
	public static final String InputInfoFieldNotFoundValidatorFail = "The input field '%1$s' could not be found.  Please add the column 'I:%1$s'.";
	public static final String StaticOptionNotFoundValidatorFail = "The option '%s' could not be found for the field '%s'.";
	public static final String BlankInputOptionValidatorFail = "The input field '%s' must not be blank.";
	public static final String MissingRequiredAssetAttributeValidatorFail = "The attribute '%s' is required.";
	public static final String ConversionValidatorFail = "The data '%s' is not valid for the field '%s.'";
    public static final String InvalidDateValidatorFail = "The date '%s' in field '%s' is not valid.  Please ensure the date is in the correct format.";
    public static final String InvalidLocationValidatorFail = "The location '%s' in field '%s' can not be found. Make sure your location is in the format 'parent > child : freeform'";
    public static final String InvalidLocationSpecificationValidatorFail = "The format for location '%s' in field '%s' is invalid. Make sure your location is in the format 'parent > child : freeform'";
    public static final String OwnerResolutionValidatorFail = "Could not find an owner for organization '%s', customer '%s' and division '%s'.";
	public static final String NonUniqueOwnerValidatorFail = "Found multiple owners for organization '%s', customer '%s' and division '%s'.";
	public static final String EventStatusValidatorFail = "'%s' is not a valid result.  Results may be 'Pass', 'Fail', or 'N/A'";
	public static final String YNValidatorFail = "'%s' is not allowed for the field '%s'.  Valid values are 'Y' or 'N'.";
	public static final String PermissionTypeFail = "User %s is not allowed to set the permission %s to %s.";
	public static final String AccountTypeFail = "The account type [%s] can not be found when trying to set its permission for user %s.";
	public static final String NoUserFoundValidationFail = "Could not find a user with the name '%s' for the field '%s'.";
	public static final String MultipleUserFoundValidationFail = "Multiple users have been found matching the name '%s'.  The matching users are: '%s'.";
	public static final String NoAssetFoundValidationFail = "Could not find an asset matching the identifier '%s' for the field '%s'.Either it doesn't exist or the event type is not associated with it.";
	public static final String MultipleAssetFoundValidationFail = "Multiple assets have been found matching the identifier '%s' for the field '%s'.";
	public static final String AssociatedEventTypeValidationFail = "The event type '%s' is not allowed for the asset type '%s'.";
	public static final String OrgWithNameNotFoundValidationFail = "The %s with name '%s' was not found.";
	public static final String CriteriaValidatorNoSectionCriteriaFail = "The column '%s:%s' does not specify a valid section and criteria";
	public static final String CriteriaValidatorDateFail = "The value '%s' is not a valid date string required by the criteria '%s'";
	public static final String CriteriaValidatorSignatureFail = "Importing of signatures not supported";
	public static final String CriteriaValidatorSelectFail = "Can't find option '%s' for select criteria %s. Expecting one of %s.";
	public static final String CriteriaOneClickFail = "Can't find option '%s' for one click criteria %s. Expecting one of %s.";
	public static final String MaxLiteUsersFail = "You can not import more than %d inspection users. (Attempted to import %d)";
	public static final String MaxReadOnlyUsersFail = "You can not import more than %d reporting users. (Attempted to import %d)";
	public static final String MaxEmployeeUersFail = "You can not import more than %d administration (full) users. (Attempted to import %d)";
    public static final String NotANumberFail = "'%s' is not a valid number for criteria '%s'";
	public static final String InvalidScoreFail = "You must enter a valid score for criteria '%s'. Value was '%s' but expecting one of %s";
	public static final String ExternalAssetMobileGuidValidatorFail = "Could not find %s '%s'";
	
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext);
}
