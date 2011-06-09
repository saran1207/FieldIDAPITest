package com.n4systems.exporting;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.persistence.Transaction;

public abstract class AbstractImporter<V extends ExternalModelView> implements Importer {
	protected static final int FIRST_DATA_ROW = 2;
	
	private final Class<V> viewClass;
	private final MapReader mapReader;
	private final Validator<ExternalModelView> validator; 
	
	private List<V> views;
	private int currentRow;
	private int totalRows;
	
	public AbstractImporter(Class<V> viewClass, MapReader mapReader, Validator<ExternalModelView> validator) {
		super();
		this.viewClass = viewClass;
		this.mapReader = mapReader;
		this.validator = validator;
	}

	protected abstract void importView(Transaction transaction, V view) throws ConversionException;
	
	/** Subclasses may override this method to do work before importing begins */
	protected void preImport(Transaction transaction) throws Exception {}
	
	@Override
	public List<ValidationResult> readAndValidate() throws IOException, ParseException, MarshalingException {
		if (views == null) {
			views = readAllViews();
		} 
		return validateAllViews();
	}
	
	@Override
	public int runImport(Transaction transaction) throws ImportException {
		if (views == null) {
			throw new IllegalStateException("runImport() called before validate()");
		}
		
		try {
			preImport(transaction);
			
			totalRows = views.size();
			currentRow = 0;
						
			for (V view: views) {
				currentRow++;
				importView(transaction, view);	
			}

		} catch (Exception e) {
			throw new ImportException("Failed to import view", e, currentRow);
		} finally {
			// clean up resources since this object could be holding a lot of them
			views = null;
			StreamUtils.close(mapReader);
		}
		
		return currentRow;
	}
	
	protected List<ValidationResult> validateAllViews() {
		List<ValidationResult> failedValidationResults = new ArrayList<ValidationResult>();
		for (int i = 0; i < views.size(); i++) {
			failedValidationResults.addAll(validator.validate(views.get(i), i + FIRST_DATA_ROW));
		}
		return failedValidationResults;
	}
	
	protected List<V> readAllViews() throws IOException, ParseException, MarshalingException {
		List<V> views = new ArrayList<V>();
		ExportMapUnmarshaler<V> unmarshaler = createMapUnmarshaler();

		Map<String, Object> row;
		while ((row = mapReader.readMap()) != null) {

			views.add(unmarshaler.toBean(row));
		}
		
		return views;
	}
	
	protected ExportMapUnmarshaler<V> createMapUnmarshaler() throws IOException, ParseException {
		return new ExportMapUnmarshaler<V>(viewClass, mapReader.getTitles());
	}
	
	@Override
	public int getCurrentRow() {
		return currentRow;
	}

	@Override
	public int getTotalRows() {
		return totalRows;
	}
}
