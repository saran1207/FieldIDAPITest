package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.UserGroupForNameExistsLoader;

import java.util.Map;

public class UserGroupExistsValidator implements FieldValidator {

    @Override
    public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
        if (fieldValue == null) {
            return ValidationResult.pass();
        }

        if (createUserGroupExistsLoader(filter).setName(fieldValue.toString()).load()) {
            return ValidationResult.pass();
        } else {
            return ValidationResult.fail(UserGroupNotFoundValidatorFail, fieldValue);
        }
    }

    protected UserGroupForNameExistsLoader createUserGroupExistsLoader(SecurityFilter filter) {
        return new UserGroupForNameExistsLoader(filter);
    }

}
