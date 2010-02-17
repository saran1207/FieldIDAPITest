package com.n4systems.exporting;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.CustomerOrgViewConverter;
import com.n4systems.api.conversion.DivisionOrgViewConverter;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.api.validation.ValidationFailedException;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.InvalidTitleException;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.persistence.savers.Saver;

public class CustomerImporter implements Importer {
	private static final int TITLE_ROW = 1;
	private static final int FIRST_DATA_ROW = 2;
	
	private final MapReader mapReader;
	private final TransactionManager transactionManager;
	private final Saver<BaseOrg> orgSaver;
	private final Validator<FullExternalOrgView> viewValidator; 
	private final CustomerOrgViewConverter customerConverter;
	private final DivisionOrgViewConverter divisionConverter;

	private List<FullExternalOrgView> orgViews;
	private List<ValidationResult> failedValidationResults;
	
	public CustomerImporter(MapReader mapReader, SecurityFilter filter) {
		 this(mapReader, new FieldIdTransactionManager(), new OrgSaver(), new ViewValidator<FullExternalOrgView>(filter), new CustomerOrgViewConverter(filter), new DivisionOrgViewConverter(filter));
	}
	
	protected CustomerImporter(MapReader mapReader, TransactionManager transactionManager, Saver<BaseOrg> orgSaver, Validator<FullExternalOrgView> viewValidator, CustomerOrgViewConverter customerConverter, DivisionOrgViewConverter divisionConverter) {
		this.mapReader = mapReader;
		this.transactionManager = transactionManager;
		this.orgSaver = orgSaver;
		this.viewValidator = viewValidator;
		this.customerConverter = customerConverter;
		this.divisionConverter = divisionConverter;
	}


	@Override
	public int validateAndImport() throws ImportException, ValidationFailedException, InvalidTitleException {
		readAllRows();		
		validateViews();

		int currentRow = FIRST_DATA_ROW;
		Transaction transaction = null;
		try {
			transaction = transactionManager.startTransaction();
						
			for (FullExternalOrgView view: orgViews) {
				
				// validation should have caught if the view is neither a customer/division
				if (view.isCustomer()) {
					importCustomer(transaction, view);
				} else if (view.isDivision()) {
					importDivision(transaction, view);
				}

				currentRow++;
			}
		} catch (ConversionException e) {
			transactionManager.finishTransaction(transaction);
			throw new ImportException("View failed model conversion", e, currentRow);
		} catch (Exception e) {
			transactionManager.rollbackTransaction(transaction);
			throw new ImportException("Failed import of model", e, currentRow);
		}
		
		return currentRow - FIRST_DATA_ROW;
	}

	private void importCustomer(Transaction transaction, FullExternalOrgView view) throws ConversionException {
		CustomerOrg customer = customerConverter.toModel(view);
		
		customer = (CustomerOrg)orgSaver.saveOrUpdate(transaction, customer);
		
		// very important: the division converter needs to be updated when the customer changes otherwise
		// it won't know what customer to set (or will be setting the wrong customer)
		divisionConverter.setParentCustomer(customer);
	}
	
	private void importDivision(Transaction transaction, FullExternalOrgView view) throws ConversionException {
		DivisionOrg division = divisionConverter.toModel(view);
		
		orgSaver.saveOrUpdate(transaction, division);
	}
	
	private void validateViews() throws ImportException, ValidationFailedException {
		failedValidationResults = new ArrayList<ValidationResult>();
		
		int currentRow = FIRST_DATA_ROW;
		for (FullExternalOrgView view: orgViews) {
			failedValidationResults.addAll(viewValidator.validate(view, currentRow));
			currentRow++;
		}
		
		if (!failedValidationResults.isEmpty()) {
			throw new ValidationFailedException(failedValidationResults);
		}
	}
	
	private void readAllRows() throws ImportException, InvalidTitleException {
		orgViews = new ArrayList<FullExternalOrgView>();

		int currentRow = TITLE_ROW;
		try {
			ExportMapUnmarshaler<FullExternalOrgView> unmarshaler = new ExportMapUnmarshaler<FullExternalOrgView>(FullExternalOrgView.class, mapReader.getTitles());
			currentRow++;
			
			FullExternalOrgView orgView;
			Map<String, String> row;
			while ((row = mapReader.readMap()) != null) {
				orgView = unmarshaler.toBean(row);
				
				orgViews.add(orgView);
				currentRow++;
			}
		
		} catch (InvalidTitleException e) {
			throw e;
		} catch (IOException e) {
			throw new ImportException("Unable to read from stream", e, currentRow);
		} catch (ParseException e) {
			throw new ImportException("Unable to parse row", e, currentRow);
		} catch (MarshalingException e) {
			throw new ImportException("Unable to convert csv to view", e, currentRow);
		}
	}
	
}
