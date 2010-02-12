package com.n4systems.api.validation;

import java.util.List;

import com.n4systems.api.model.ExternalModelView;

public interface Validator<V extends ExternalModelView> {

	/**
	 * Returns a list of ValidationResults in failure state.  If all validations passed, this list will be empty.
	 */
	public List<ValidationResult> validate(V view);

	/**
	 * Returns a list of ValidationResults in failure state.  If all validations passed, this list will be empty.
	 * The row will be passed on to the returned failed results.
	 */
	public List<ValidationResult> validate(V view, int row);

}