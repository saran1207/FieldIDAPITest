package com.n4systems.exporting;

import java.util.List;

import com.n4systems.api.validation.ValidationResult;


public interface Importer {
	public boolean validateAndImport() throws ImportException;
	public List<ValidationResult> getFailedValidationResults();
}
