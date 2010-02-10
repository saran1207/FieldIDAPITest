package com.n4systems.api.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.validators.FieldValidator;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.util.reflection.Reflector;

public class ViewValidator<V extends ExternalModelView> {
	private final ValidatorFactory validatorFactory;
	
	public ViewValidator() {
		this(new CachedValidatorFactory());
	}
	
	public ViewValidator(ValidatorFactory validatorFactory) {
		this.validatorFactory = validatorFactory;
	}
	
	/**
	 * Returns a list of ValidationResults in failure state.  If all validations passed, this list will be empty.
	 */
	public List<ValidationResult> validate(V view) {
		List<ValidationResult> failedResults = new ArrayList<ValidationResult>();
		
		ValidationResult failedResult;
		Field[] fields = Reflector.findAllFieldsWithAnnotation(view.getClass(), ExportField.class);
		for (Field field: fields) {
			failedResult = validateField(view, field);
			
			// the validation may have passed in which case failedResult will be null
			if (failedResult != null) {
				failedResults.add(failedResult);
			}
		}
		
		return failedResults;
	}
	
	/**
	 * Runs the FieldValidators in the order they were defined, against a Field and returns the first 
	 * failed ValidationResult or null if there were no failures.  We need to stop after the first 
	 * failed result as later validators may be relying on the pass state of earlier validators.
	 * An example would be having a NotNullValidator before an EmailValidator
	 */
	private ValidationResult validateField(V view, Field field) {
		ValidationResult firstFailedResult = null;
		
		ExportField exportField = field.getAnnotation(ExportField.class);
		
		Object fieldValue;
		FieldValidator validator;
		ValidationResult result;
		// note it is very important the validators are run in order
		for (Class<? extends FieldValidator> validatorClass: exportField.validators()) {
			validator = validatorFactory.create(validatorClass);
			
			fieldValue = getFieldValue(view, field);
			result = validator.validate(fieldValue, view, exportField.title());
			
			// any failure will stop us from running the rest of the validators
			// see javadocs at the top of this method
			if (result.isFailed()) {
				firstFailedResult = result;
				break;
			}
		}
		
		return firstFailedResult;
	}
	
	private Object getFieldValue(V view, Field field) {
		try {
			return BeanUtils.getProperty(view, field.getName());
		} catch (Exception e) {
			String msg = String.format("Failed getting field [%s] on class [%s]", field.getName(), view.getClass().getName());
			throw new ValidationException(msg, e);
		}
	}
}
