package com.n4systems.fieldid.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.VisitorFieldValidator;

/**
 * This was pulled from  a Struts 2 form.  
 * It may have been implemented in a later version of XWork but we don't have it yet.
 *
 */
public class ConditionalVisitorFieldValidator extends VisitorFieldValidator {
	private String expression;

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	/**
	 * If expression evaluates to true, invoke visitor validation.
	 * 
	 * @param object
	 *            the object being validated
	 * @throws ValidationException
	 */
	public void validate(Object object) throws ValidationException {
		if (validateExpression(object)) {
			super.validate(object);
		}
	}

	/**
	 * Validate the expression contained in the "expression" paramter.
	 * 
	 * @param object
	 *            the object you're validating
	 * @return true if expression evaluates to true (implying a validation
	 *         failure)
	 * @throws ValidationException
	 *             if anything goes wrong
	 */
	public boolean validateExpression(Object object) throws ValidationException {
		Boolean answer = Boolean.FALSE;
		Object obj = null;

		try {
			obj = getFieldValue(expression, object);
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			// let this pass
		}

		if ((obj != null) && (obj instanceof Boolean)) {
			answer = (Boolean) obj;
		} 

		return answer;
	}
}
