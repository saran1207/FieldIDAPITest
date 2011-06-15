package com.n4systems.api.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.validators.FieldValidator;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.reflection.Reflector;

public class ViewValidator implements Validator<ExternalModelView> {
	private final ValidatorFactory validatorFactory;
	private final SecurityFilter filter;
	private final Map<String, Object> validationContext = new HashMap<String, Object>();
	
	public ViewValidator(SecurityFilter filter) {
		this(filter, new CachedValidatorFactory());
	}
	
	public ViewValidator(SecurityFilter filter, ValidatorFactory validatorFactory) {
		this.filter = filter;
		this.validatorFactory = validatorFactory;
	}
	
	@Override
	public List<ValidationResult> validate(ExternalModelView view) {
		return validate(view, 0);
	}
	
	@Override
	public List<ValidationResult> validate(ExternalModelView view, int row) {
		List<ValidationResult> failedResults = new ArrayList<ValidationResult>();
		
		ValidationResult failedResult;
		Field[] fields = Reflector.findAllFieldsWithAnnotation(view.getClass(), SerializableField.class);
		for (Field field: fields) {
			failedResult = validateField(view, field);
			
			// the validation may have passed in which case failedResult will be null
			if (failedResult != null) {
				failedResult.setRow(row);
				failedResults.add(failedResult);
			}
		}
		
		return failedResults;
	}
	
	@Override
	public Map<String, Object> getValidationContext() {
		return validationContext;
	}
	
	/**
	 * Runs the FieldValidators in the order they were defined, against a Field and returns the first 
	 * failed ValidationResult or null if there were no failures.  We need to stop after the first 
	 * failed result as later validators may be relying on the pass state of earlier validators.
	 * An example would be having a NotNullValidator before an EmailValidator
	 */
	private ValidationResult validateField(ExternalModelView view, Field field) {
		ValidationResult firstFailedResult = null;
		
		SerializableField exportField = field.getAnnotation(SerializableField.class);
		
		Object fieldValue;
		FieldValidator validator;
		ValidationResult result;
		// note it is very important the validators are run in order
		for (Class<? extends FieldValidator> validatorClass: exportField.validators()) {
			validator = validatorFactory.create(validatorClass);
			
			fieldValue = getFieldValue(view, field);
			result = validator.validate(fieldValue, view, exportField.title(), filter, exportField, validationContext);
			
			// any failure will stop us from running the rest of the validators
			// see javadocs at the top of this method
			if (result.isFailed()) {
				firstFailedResult = result;
				break;
			}
		}
		
		return firstFailedResult;
	}
	
	private Object getFieldValue(ExternalModelView view, Field field) {
		try {
			// TODO: this code is idential to the SerializationHandler.getFieldValue().  It should be refactored into a common place.
			PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
			Object property = propertyUtils.getProperty(view, field.getName());
			return property;
		} catch (Exception e) {
			String msg = String.format("Failed getting field [%s] on class [%s]", field.getName(), view.getClass().getName());
			throw new ValidationException(msg, e);
		}
	}
	
}
