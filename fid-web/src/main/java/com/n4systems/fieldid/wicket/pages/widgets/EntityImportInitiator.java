package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.ImportTaskRegistry;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.ImporterFactory;
import com.n4systems.exporting.beanutils.InvalidTitleException;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.EmptyDocumentException;
import com.n4systems.exporting.io.ExcelXSSFMapReader;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ImportTask;
import com.n4systems.util.ArrayUtils;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
import rfid.web.helper.SessionUser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

/**
 * Helper class to handle the boilerplate aspect of starting an entity import.
 */
abstract public class EntityImportInitiator {

    private static final Logger logger = Logger.getLogger(EntityImportInitiator.class);

    private WebSessionMap webSessionMap;
    private User currentUser;
    private SessionUser sessionUser;
    private SecurityFilter securityFilter;

    public EntityImportInitiator(WebSessionMap webSessionMap, User currentUser, SessionUser sessionUser, SecurityFilter securityFilter) {
        this.webSessionMap = webSessionMap;
        this.currentUser = currentUser;
        this.sessionUser = sessionUser;
        this.securityFilter = securityFilter;
    }

    public ImportResultStatus doImport(InputStream importDoc) {
        if (importDoc == null) {
            return new ImportResultStatus(false, null,
                    new FIDLabelModel("error.file_required").getObject(), null);
        }
        else {
            return runImport(importDoc, new ImportTaskRegistry());
        }
    }

    private ImportResultStatus runImport(InputStream importDoc, ImportTaskRegistry taskRegistry) {
        String taskId = null;
        try {
            // This case shouldn't happen since the form should not allow you to submit when one is already registered
            if (isImportRunning(taskRegistry)) {
                return new ImportResultStatus(false, null,
                        new FIDLabelModel("error.import_already_running").getObject(), null);
            } else {
                taskRegistry.remove(getImportTaskId());
                webSessionMap.clearImportTaskId();
            }

            Importer importer = createAndValidateImporter(importDoc);
            List<ValidationResult> failedImportValidationResults = importer.readAndValidate();

            if (!failedImportValidationResults.isEmpty()) {
                return new ImportResultStatus(false, failedImportValidationResults,
                        new FIDLabelModel("label.validation_failed").getObject(),null);
            }
            taskId = executeImportTask(importer, taskRegistry);
        } catch (EmptyDocumentException e) {
            return new ImportResultStatus(false, null,
                    new FIDLabelModel("error.empty_import_document").getObject(), null);
        } catch (InvalidTitleException e) {
            return new ImportResultStatus(false, null,
                    new FIDLabelModel("error.bad_file_format", ArrayUtils.newArray(e.getTitle())).getObject(), null);
        } catch (Exception e) {
            // if the file is not an excel file, the exception that comes back will be a BifException contained inside an IOException
            if (e.getCause() instanceof BiffException) {
                logger.warn(String.format("Import failed for User [%s]", currentUser.toString()), e.getCause());
                return new ImportResultStatus(false, null,
                        new FIDLabelModel("error.unsupported_content_type").getObject(), null);
            } else {
                // we don't know exactly what happened here, log it and fail generically
                logger.error(String.format("Import failed for User [%s]", currentUser.toString()), e);
                return new ImportResultStatus(false, null, new FIDLabelModel("error.import_failed").getObject(), null);
            }
        }
        return new ImportResultStatus(true, null, null, taskId); // SUCCESS;
    }

    private Importer createAndValidateImporter(InputStream importDoc)
            throws IOException, ParseException, MarshalingException {

        MapReader mapReader = new ExcelXSSFMapReader(importDoc, sessionUser.getTimeZone());
        Importer importer = createImporter(mapReader);
        return importer;
    }

    private String executeImportTask(Importer importer, ImportTaskRegistry taskRegistry) {
        ImportTask task = new ImportTask(importer, createSuccessNotification(), createFailureNotification());

        TaskExecutor.getInstance().execute(task);

        // don't register the task and id until after we've sent
        // to the executor, in case it was rejected
        String taskId = task.getId();
        setImportTaskId(taskId);
        taskRegistry.register(task);
        return taskId;
    }

    protected ImporterFactory getImporterFactory() {
        return new ImporterFactory(securityFilter);
    }

    private boolean isImportRunning(ImportTaskRegistry taskRegistry) {
        return (getImportTaskId() != null && !(taskRegistry.get(getImportTaskId())).isCompleted());
    }

    private String getImportTaskId() {
        return webSessionMap.getImportTaskId();
    }

    private void setImportTaskId(String taskId) {
        webSessionMap.setImportTaskId(taskId);
    }

    /*
      Example implementations would be:
          getImporterFactory().createAssetImporter(reader, ...
          getImporterFactory().createEventImporter(reader, ...
          getImporterFactory().createCustomerImporter(reader, ...
          getImporterFactory().createUserImporter(reader, ...
   */
    abstract protected Importer createImporter(MapReader reader);

    abstract protected ImportSuccessNotification createSuccessNotification();

    abstract protected ImportFailureNotification createFailureNotification();
}
