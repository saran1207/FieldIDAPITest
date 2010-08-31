package com.n4systems.api.validation;

import java.util.List;

public class ValidationFailedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private final List<ValidationResult> failedValidationResults;

	public ValidationFailedException(List<ValidationResult> failedValidationResults) {
		this.failedValidationResults = failedValidationResults;
	}

	public List<ValidationResult> getFailedValidationResults() {
		return failedValidationResults;
	}
	
}
