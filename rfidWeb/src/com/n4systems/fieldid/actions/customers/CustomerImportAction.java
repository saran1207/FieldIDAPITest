package com.n4systems.fieldid.actions.customers;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.api.validation.ValidationFailedException;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.CustomerImporter;
import com.n4systems.exporting.beanutils.InvalidTitleException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapReaderFactory;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.ArrayUtils;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
@SuppressWarnings("serial")
public class CustomerImportAction extends AbstractAction {	
	private Logger logger = Logger.getLogger(CustomerImportAction.class);

	private final MapReaderFactory mapReaderFactory;
	private File importDoc;
    private String importDocContentType;
	private List<ValidationResult> failedValidationResults;
	
	public CustomerImportAction(PersistenceManager persistenceManager) {
		this(persistenceManager, new MapReaderFactory());
	}

	public CustomerImportAction(PersistenceManager persistenceManager, MapReaderFactory mapReaderFactory) {
		super(persistenceManager);
		this.mapReaderFactory = mapReaderFactory;
	}
	
	@SkipValidation
	public String doShow() {
		return SUCCESS;
	}
	
	public String doImport() {
		if (importDoc == null) {
			addActionError(getText("error.file_required"));
			return MISSING;
		}
		
		if (!mapReaderFactory.isSupportedContentType(importDocContentType)) {
			addActionError(getText("error.unsupported_content_type"));
			return MISSING;
		}
		
		String status = runImport();
		
		return status;
	}

	private String runImport() {
		String status = SUCCESS;
		try {
			MapReader mapReader = mapReaderFactory.createMapReader(new FileInputStream(importDoc), importDocContentType);
			
			CustomerImporter importer = new CustomerImporter(mapReader, getSecurityFilter());
			
			int orgsImported = importer.validateAndImport();
			
			addActionMessage(getText("label.import_success", ArrayUtils.newArray(String.valueOf(orgsImported))));
		
		} catch (InvalidTitleException e) {
			addActionError(getText("error.bad_file_format", ArrayUtils.newArray(e.getTitle())));
			status = INPUT;			
		} catch (ValidationFailedException e) {
			failedValidationResults = e.getFailedValidationResults();
			addActionError(getText("label.validation_failed"));
			status = INPUT;
		} catch (Exception e) {
			logger.error(String.format("Import Customers failed for User [%s]", getUser().toString()), e);
			addActionError(getText("error.import_failed"));
			status = ERROR;
		}
		return status;
	}

	public void setImportDoc(File importDoc) {
		this.importDoc = importDoc;
	}

	public void setImportDocContentType(String importDocContentType) {
		this.importDocContentType = importDocContentType;
	}

	public List<ValidationResult> getFailedValidationResults() {
		return failedValidationResults;
	}
	
	public boolean isValidationFailed() {
		return (failedValidationResults != null) && !failedValidationResults.isEmpty();
	}
}
