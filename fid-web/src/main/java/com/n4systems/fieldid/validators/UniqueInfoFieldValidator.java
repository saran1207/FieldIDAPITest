package com.n4systems.fieldid.validators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.viewhelpers.TrimmedString;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UniqueInfoFieldValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object action) throws ValidationException {

		String fieldName = getFieldName();
		List<TrimmedString> set = (List<TrimmedString>) getFieldValue(fieldName, action);
		StrutsListHelper.clearNulls(set);
		
		if (set == null || set.isEmpty()) {
			return;
		} else {
			Set<TrimmedString> duplicatesRemoved = new HashSet<TrimmedString>(set);
			
			if (duplicatesRemoved.size() != set.size()) {
				addFieldError(fieldName, action);
				return;
			}
		}
	}
}
