package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.security.PasswordComplexityChecker;

public class PasswordValidator implements FieldValidator {
		
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, ExportField field, Map<String, Object> validationContext) {
		String password = (String) fieldValue;

		// CAVEAT : this assumes UserView.   reasonable assumption since it's dealing with passwords.  if required by other views then suggest making
		//   a HasPassword interface that the view can implement.
		UserView userView = (UserView) view;

		// TODO DD : add unit tests.
		if (YNField.Y.toString().equals(userView.getAssignPassword())) {
			if (password==null) { 
				return ValidationResult.fail("can't have empty password if you have specified 'Assign Password' to true");
			}
			PasswordComplexityChecker passwordComplexityChecker = PasswordComplexityChecker.createDefault();		
			
			// TODO DD : need to generate suitable error message for weak password failures.
			return passwordComplexityChecker.isValid(password) ? ValidationResult.fail("password " + password + " is not strong enough.") : ValidationResult.pass();				
		}
		return ValidationResult.pass();		
	}

}
