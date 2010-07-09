package com.n4systems.api.validation.validators;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.Column;

import org.apache.log4j.Logger;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exceptions.Defect;
import com.n4systems.model.security.SecurityFilter;

public abstract class ProductViewStringFieldLengthValidator  implements FieldValidator  {

	private static final Logger logger = Logger.getLogger(ProductViewToProductRfidLengthValidator.class);
	private static final int DEFAULT_MAX_LENGTH = 255;
	
	
	private final int maxLength;
		
	public ProductViewStringFieldLengthValidator() {
		maxLength = retrieveMaxLengthFromField(getField());
	}
	
	protected abstract Field getField();
	
	
	
	private int retrieveMaxLengthFromField(Field field) {
		if (field == null) {
			logger.warn("Serial/Rfid number length validator could not find field");
			return DEFAULT_MAX_LENGTH;
		} 
		
		Column col = field.getAnnotation(Column.class);
		if (col == null) {
			logger.warn("Serial/Rfid number length validator could not find column annotation");
			return DEFAULT_MAX_LENGTH;
		} 
			
		return col.length();
	}

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, Map<String, Object> validationContext) {
		gaurd(fieldValue);
		
		
		if (fieldValue == null ||	((String) fieldValue).length() <= maxLength) {
			return ValidationResult.pass();
		} else {
			return ValidationResult.fail(ProductViewStringLengthValidatorFail, fieldName, maxLength);
		}
	}

	private void gaurd(Object fieldValue) {
		if (fieldValue != null && !(fieldValue instanceof String)) {
			throw new Defect("this validator may only operate on a string field.");
		}
		
	}

	

}