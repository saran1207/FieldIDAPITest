package com.n4systems.api.validation;

public class ValidationResult {
	
	public static ValidationResult pass() {
		return new ValidationResult(true, null);
	}

	public static ValidationResult fail(String messageFormat, Object...args) {
		return fail(String.format(messageFormat, args));
	}
	
	public static ValidationResult fail(String message) {
		return new ValidationResult(false, message);
	}

	private final boolean passed;
	private final String message;
	private int row;
	
	public ValidationResult(boolean passed, String message) {
		this.passed = passed;
		this.message = message;
	}

	public boolean isPassed() {
		return passed;
	}
	
	public boolean isFailed() {
		return !passed;
	}

	public String getMessage() {
		return message;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
}
