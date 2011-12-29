package com.n4systems.api.validation.validators;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.security.PermissionType;
import com.n4systems.security.UserType;

public class PermissionValidator implements FieldValidator {
	
	public static final Map<String, PermissionType> permissionsMap = new HashMap<String, PermissionType>();

	static { 
		permissionsMap.put(UserView.IDENTIFY_ASSETS_FIELD, PermissionType.Tag);
		permissionsMap.put(UserView.MANAGE_SYSTEM_CONFIGURATION_FIELD, PermissionType.ManageSystemConfig);
		permissionsMap.put(UserView.MANAGE_SYSTEM_USERS_FIELD, PermissionType.ManageSystemUsers);
		permissionsMap.put(UserView.MANAGE_JOB_SITES_FIELD, PermissionType.ManageEndUsers);
		permissionsMap.put(UserView.CREATE_EVENTS_FIELD, PermissionType.CreateEvent);
		permissionsMap.put(UserView.EDIT_EVENTS_FIELD, PermissionType.EditEvent);
		permissionsMap.put(UserView.MANAGE_JOBS_FIELD, PermissionType.ManageJobs);
		permissionsMap.put(UserView.MANAGE_SAFETY_NETWORK_FIELD, PermissionType.ManageSafetyNetwork);
		permissionsMap.put(UserView.ACCESS_WEB_STORE_FIELD, PermissionType.AccessWebStore);
	}
		
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		String yn = (String)fieldValue;
		UserView userView = ((UserView) view);		
		if (YNField.isYes(yn)) { 
			UserType userType = getUserTypeFromView(view);  // CAVEAT : this validator is tied to UserView...can't be shared among different views.
			if (userType==null) { 
				return ValidationResult.fail(AccountTypeFail,userView.getAccountType(), userView.getUserID() );
			} else if (UserType.hasPermission(userType, getPermissionFromViewName(fieldName))) { 
				return ValidationResult.pass(); 
			} else { 
				return ValidationResult.fail(PermissionTypeFail, userView.getUserID(), fieldName, yn);
			}					
		} else if (!YNField.isNo(yn) && yn!=null) { 
			return ValidationResult.fail(YNValidatorFail, yn, fieldName);
		}
		return ValidationResult.pass();	
	}
	
	private PermissionType getPermissionFromViewName(String permissionViewName) {
		if (permissionViewName==null) { 
			return null;
		}
		return permissionsMap.get(permissionViewName);
	}

	private UserType getUserTypeFromView(ExternalModelView view) {
		UserView userView = (UserView) view;
		return UserType.valueFromLabel(userView.getAccountType()); 		
	}
	
}
