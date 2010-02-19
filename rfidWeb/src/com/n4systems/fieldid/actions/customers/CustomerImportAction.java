package com.n4systems.fieldid.actions.customers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.CustomerImporter;
import com.n4systems.exporting.ImportTaskRegistry;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.beanutils.InvalidTitleException;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapReaderFactory;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.CustomerImportTask;
import com.n4systems.util.ArrayUtils;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
@SuppressWarnings("serial")
public class CustomerImportAction extends AbstractAction {	
	private Logger logger = Logger.getLogger(CustomerImportAction.class);

	private final ImportTaskRegistry taskRegistry;
	private final Executor executor;
	private final MapReaderFactory mapReaderFactory;
	private File importDoc;
    private String importDocContentType;
	private List<ValidationResult> failedValidationResults; 
	
	public CustomerImportAction(PersistenceManager persistenceManager) {
		this(persistenceManager, new MapReaderFactory(), TaskExecutor.getInstance(), new ImportTaskRegistry());
	}

	public CustomerImportAction(PersistenceManager persistenceManager, MapReaderFactory mapReaderFactory, Executor executor, ImportTaskRegistry taskRegistry) {
		super(persistenceManager);
		this.mapReaderFactory = mapReaderFactory;
		this.executor = executor;
		this.taskRegistry = taskRegistry;
	}
	
	@SkipValidation
	public String doShow() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doGetStatus() {
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
		try {
			// This case shouldn't happen since the form should not allow you to submit when one is already registered
			if (isImportRunning()) {
				addActionError(getText("error.import_already_running"));
				return ERROR;
			} else {
				taskRegistry.remove(getImportTaskId());
				getSession().clearImportTaskId();
			}
			
			Importer importer = createAndValidateImporter();
			
			if (!failedValidationResults.isEmpty()) {
				addActionError(getText("label.validation_failed"));
				return INPUT;
			}

			executeImportTask(importer);
			
		} catch (InvalidTitleException e) {
			addActionError(getText("error.bad_file_format", ArrayUtils.newArray(e.getTitle())));
			return INPUT;	
		} catch (Exception e) {
			logger.error(String.format("Import Customers failed for User [%s]", getUser().toString()), e);
			addActionError(getText("error.import_failed"));
			return ERROR;
		}
		
		return SUCCESS;
	}

	private void executeImportTask(Importer importer) {
		CustomerImportTask task = new CustomerImportTask(importer, getUser());		
		
		executor.execute(task);
		
		// don't register the task and id until after we've sent
		// to the executor, in case it was rejected
		setImportTaskId(task.getId());
		
		taskRegistry.register(task);
	}

	private Importer createAndValidateImporter() throws IOException, FileNotFoundException, ParseException, MarshalingException {
		MapReader mapReader = mapReaderFactory.createMapReader(new FileInputStream(importDoc), importDocContentType);
		
		Importer importer = new CustomerImporter(mapReader, getSecurityFilter());
		
		failedValidationResults = importer.readAndValidate();
		
		return importer;
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
	
	public boolean isImportRunning() {
		return (getImportTaskId() != null && !getTask().isCompleted());
	}
	
	protected void setImportTaskId(String taskId) {
		getSession().setImportTaskId(taskId);
	}
	
	protected String getImportTaskId() {
		return getSession().getImportTaskId();
	}
	
	public CustomerImportTask getTask() {
		return taskRegistry.get(getImportTaskId());
	}
}
