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
		// make sure it's a Y/N value first.
		if (fieldValue==null) { 
			return ValidationResult.pass();
		}
		String yn = ((String) (fieldValue)).trim().toUpperCase();
		if ( YNField.Y.equals(YNField.valueOf(yn))) { 			
			UserType userType = getUserTypeFromView(view);  // CAVEAT : this validator is tied to UserView...can't be shared among different views.			
			if ( UserType.hasPermission(userType, getPermissionFromViewName(fieldName)) ) { 
				return ValidationResult.pass(); 
			} else { 
				 ValidationResult.fail("permission " + fieldValue + " not allowed for account type " + userType);
			}					
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
