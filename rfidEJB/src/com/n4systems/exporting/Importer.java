package com.n4systems.exporting;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.MarshalingException;


public interface Importer {
	
	/**
	 * Validates the import and returns the validation results.  Validation has passed if the list returned is empty.
	 */
	public List<ValidationResult> readAndValidate() throws  IOException, ParseException, MarshalingException;
	
	/**
	 * Imports all rows.  validate() MUST be called prior to calling runImport.  Failure to do so
	 * will result in an IllegalStateException.  Returns a count of the number of imported rows.
	 * Throws ValidationFailedException if validation failed.  The ValidationFailedException will
	 * contain a list of failed validation results
	 */
	public int runImport() throws ImportException;
	
	/**
	 * @return Returns the total number of rows to import
	 */
	public int getTotalRows();
	
	/**
	 * @return Returns the current row being worked on
	 */
	public int getCurrentRow();
	
}
