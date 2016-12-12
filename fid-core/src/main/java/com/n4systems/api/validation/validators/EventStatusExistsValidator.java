package com.n4systems.api.validation.validators;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.eventstatus.EventStatusForNameExistsLoader;
import com.n4systems.model.security.SecurityFilter;

import java.util.Map;

public class EventStatusExistsValidator implements FieldValidator {

    @Override
    public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
        if (fieldValue == null) {
			return ValidationResult.pass();
		}

		String name = (String)fieldValue;
        EventStatusForNameExistsLoader statusExistsLoader = createEventStatusForNameExistsLoader(filter).setName(name);

        if (statusExistsLoader.load()) {
            return ValidationResult.pass();
        } else {
           return ValidationResult.fail(NamedFieldNotFoundValidatorFail, fieldName, name);
        }
    }

    protected EventStatusForNameExistsLoader createEventStatusForNameExistsLoader(SecurityFilter filter) {
        return new EventStatusForNameExistsLoader(filter);
    }
}
