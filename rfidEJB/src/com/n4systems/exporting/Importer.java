package com.n4systems.exporting;

import com.n4systems.api.validation.ValidationFailedException;


public interface Importer {
	/**
	 * Validates and Imports all rows.  Returns a count of the number of imported organizations.
	 * Throws ValidationFailedException if validation failed.  The ValidationFailedException will
	 * contain a list of failed validation results
	 */
	public int validateAndImport() throws ImportException, ValidationFailedException;
}
