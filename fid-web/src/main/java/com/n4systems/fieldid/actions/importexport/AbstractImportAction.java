package com.n4systems.fieldid.actions.importexport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Executor;

import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.ImportTaskRegistry;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.ImporterFactory;
import com.n4systems.exporting.beanutils.InvalidTitleException;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.EmptyDocumentException;
import com.n4systems.exporting.io.ExcelMapReader;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ImportTask;
import com.n4systems.util.ArrayUtils;

@SuppressWarnings("serial")
public abstract class AbstractImportAction extends AbstractAction {
	private Logger logger = Logger.getLogger(AbstractImportAction.class);

	private final ImportTaskRegistry taskRegistry;
	private final Executor executor;
	
	private File importDoc;
    private List<ValidationResult> failedValidationResults; 
    
    public AbstractImportAction(PersistenceManager persistenceManager) {
		this(persistenceManager, TaskExecutor.getInstance(), new ImportTaskRegistry());
	}

	public AbstractImportAction(PersistenceManager persistenceManager, Executor executor, ImportTaskRegistry taskRegistry) {
		super(persistenceManager);
		this.executor = executor;
		this.taskRegistry = taskRegistry;
	}

	protected abstract Importer createImporter(MapReader reader);
	protected abstract ImportSuccessNotification createSuccessNotification();
	protected abstract ImportFailureNotification createFailureNotification();
	
	@SkipValidation
	public String doShow() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doGetStatus() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doImport() {
		if (importDoc == null) {
			addActionErrorText("error.file_required");
			return MISSING;
		}
		
		String status = runImport();
		return status;
	}
	
	private String runImport() {
		try {
			// This case shouldn't happen since the form should not allow you to submit when one is already registered
			if (isImportRunning()) {
				addActionErrorText("error.import_already_running");
				return ERROR;
			} else {
				taskRegistry.remove(getImportTaskId());
				getSession().clearImportTaskId();
			}
			
			Importer importer = createAndValidateImporter();
			
			if (!failedValidationResults.isEmpty()) {
				addActionErrorText("label.validation_failed");
				return INPUT;
			}

			executeImportTask(importer);
		} catch (EmptyDocumentException e) {
			addActionErrorText("error.empty_import_document");
			return INPUT;
		} catch (InvalidTitleException e) {
			addActionError(getText("error.bad_file_format", ArrayUtils.newArray(e.getTitle())));
			return INPUT;	
		} catch (Exception e) {
			// if the file is not an excel file, the exception that comes back will be a BifException contained inside an IOException
			if (e.getCause() instanceof BiffException) {
				logger.warn(String.format("Import failed for User [%s]", getUser().toString()), e.getCause());
				addActionErrorText("error.unsupported_content_type");
			} else {
				// we don't know exactly what happened here, log it and fail generically 
				logger.error(String.format("Import failed for User [%s]", getUser().toString()), e);
				addActionErrorText("error.import_failed");
			}
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	private void executeImportTask(Importer importer) {
		ImportTask task = new ImportTask(importer, createSuccessNotification(), createFailureNotification());		
		
		executor.execute(task);
		
		// don't register the task and id until after we've sent
		// to the executor, in case it was rejected
		setImportTaskId(task.getId());
		
		taskRegistry.register(task);
	}
	
	private Importer createAndValidateImporter() throws IOException, FileNotFoundException, ParseException, MarshalingException {
		MapReader mapReader = new ExcelMapReader(new FileInputStream(importDoc), getSessionUser().getTimeZone());
		
		Importer importer = createImporter(mapReader);
		
		failedValidationResults = importer.readAndValidate();
		
		return importer;
	}
	
	protected ImporterFactory getImporterFactory() {
		return new ImporterFactory(getSecurityFilter());
	}
	
	protected void setImportTaskId(String taskId) {
		getSession().setImportTaskId(taskId);
	}
	
	protected String getImportTaskId() {
		return getSession().getImportTaskId();
	}
	
	public ImportTask getTask() {
		return taskRegistry.get(getImportTaskId());
	}
	
	public boolean isImportRunning() {
		return (getImportTaskId() != null && !getTask().isCompleted());
	}
	
	public List<ValidationResult> getFailedValidationResults() {
		return failedValidationResults;
	}
	
	public boolean isValidationFailed() {
		return (failedValidationResults != null) && !failedValidationResults.isEmpty();
	}
	
	public void setImportDoc(File importDoc) {
		this.importDoc = importDoc;
	}
}
