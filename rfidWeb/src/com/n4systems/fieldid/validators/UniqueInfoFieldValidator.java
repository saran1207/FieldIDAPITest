package com.n4systems.fieldid.validators;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UniqueInfoFieldValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object action) throws ValidationException {

		String fieldName = getFieldName();
		Collection<String> set = (Collection<String>)getFieldValue(fieldName, action);

		if (set == null || set.isEmpty()) {
			return;
		} else {
			Set<String> duplicatesRemoved = new LinkedHashSet<String>(set);
			if (duplicatesRemoved.size() != set.size()) {
				addFieldError(fieldName, action);
				return;
			}
		}
	}
}
