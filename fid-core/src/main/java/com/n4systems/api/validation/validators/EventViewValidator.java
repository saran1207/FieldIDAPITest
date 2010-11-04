package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.EventView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;

public abstract class EventViewValidator implements FieldValidator {
	public static final String EVENT_TYPE_KEY = "eventType";
	
	protected abstract ValidationResult validate(Object fieldValue, EventView view, String fieldName, SecurityFilter filter, EventType type);
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		return validate(fieldValue, (EventView)view, fieldName, filter, (EventType)validationContext.get(EVENT_TYPE_KEY));
	}

}
