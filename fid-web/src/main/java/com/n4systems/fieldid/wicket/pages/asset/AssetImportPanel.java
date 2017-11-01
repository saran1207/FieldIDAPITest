package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.AssetExporter;
import com.n4systems.exporting.ImportTaskRegistry;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.ImporterFactory;
import com.n4systems.exporting.beanutils.InvalidTitleException;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.*;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.AssetImportFailureNotification;
import com.n4systems.notifiers.notifications.AssetImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ImportTask;
import com.n4systems.util.ArrayUtils;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.string.StringValue;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.web.helper.SessionUser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by agrabovskis on 2017-10-31.
 */
public class AssetImportPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AssetImportPanel.class);

    private LoaderFactory loaderFactory;
    private IModel<AssetType> selectedAssetType;
    private IModel<User> currentUserModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<WebSessionMap> webSessionMapModel;

    public AssetImportPanel(String id, StringValue preSelectedAssetTypeId, IModel<User> currentUserModel,
                            IModel<SessionUser> sessionUserModel, IModel<SecurityFilter> securityFilterModel,
                            IModel<WebSessionMap> webSessionMapModel) {
        super(id);
        if (preSelectedAssetTypeId != null && !preSelectedAssetTypeId.isEmpty()) {
            selectedAssetType = Model.of(getAssetType(new Long(preSelectedAssetTypeId.toString())));
        }
        else {
            selectedAssetType = Model.of((AssetType)null);
        }
        this.currentUserModel = currentUserModel;
        this.sessionUserModel = sessionUserModel;
        this.securityFilterModel = securityFilterModel;
        this.webSessionMapModel = webSessionMapModel;
        addComponents();
    }

    private void addComponents() {

        final DropDownChoice<AssetType> assetTypeSelection = new DropDownChoice<AssetType>("assetTypesSelectionList",
                selectedAssetType,
                new LoadableDetachableModel<List<AssetType>>() {
                    @Override
                    protected List<AssetType> load() {
                        return getLoaderFactory().createAssetTypeListLoader().load();
                    }
                },
                new IChoiceRenderer<AssetType>() {
                    @Override
                    public Object getDisplayValue(AssetType object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(AssetType object, int index) {
                        return object.getId().toString();
                    }
                }
        ) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

        };
        add(assetTypeSelection);

        Link downloadTemplateLink = new Link("downloadTemplateLink") {

            @Override
            public void onClick() {
                System.out.println("Download template onClick");
                AssetType assetType = getSelectedAssetType();
                System.out.println("... selected asset type " + assetType.getName());
                AbstractResourceStreamWriter rStream = doDownloadExample(assetType);
                System.out.println("... of type " + rStream.getContentType());
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(assetType));
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };
        add(downloadTemplateLink);

        final FileUploadField fileUploadField = new FileUploadField("fileToUpload");
        Form fileUploadForm = new Form("fileUploadForm") {
            @Override
            protected void onSubmit() {
                System.out.println("File upload submitted");
                ImportResultStatus result;
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    try {
                        System.out.println("Uploading " + fileUploadField.getFileUpload().getClientFileName());
                        InputStream inputStream = fileUploadField.getFileUpload().getInputStream();
                        result = doImport(inputStream, getSelectedAssetType());
                    } catch (IOException ex) {
                        logger.error("Exception reading input file", ex);
                        result = new ImportResultStatus(false, null,
                                new FIDLabelModel("error.io_error_reading_import_file").getObject(), null);
                    }
                }
                else {
                    result = new ImportResultStatus(false, null,
                            new FIDLabelModel("error.file_required").getObject(), null);
                }
                AssetImportResultPage resultPage = new AssetImportResultPage(selectedAssetType.getObject().getId(), result);
                setResponsePage(resultPage);
            }
        };
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);
        add(fileUploadForm);
    }

    private AssetType getSelectedAssetType() {
        Long id = selectedAssetType.getObject().getId();
        // Get fully populated AssetType object
        return getAssetType(id);
    }

    private AssetType getAssetType(Long id) {
        return getLoaderFactory().createAssetTypeLoader().setStandardPostFetches().setId(id).load();
    }

    private AbstractResourceStreamWriter doDownloadExample(AssetType assetType) {
        System.out.println("Called doDownloadExample");
        AssetExporter exporter = new AssetExporter(getLoaderFactory().createPassthruListLoader(Arrays.asList(createExampleAsset(assetType))));

        AbstractResourceStreamWriter rStream = new AbstractResourceStreamWriter() {
            @Override
            public void write(Response output) {
                MapWriter writer = null;
                try {
                    writer = new ExcelXSSFMapWriter(new DateTimeDefiner(getCurrentUser()));
                    exporter.export(writer);
                    ((ExcelXSSFMapWriter)writer).writeToStream(getResponse().getOutputStream());

                } catch (Exception e) {
                    logger.error("Failed generating example asset export", e);
                    //TODO handle this error?
                } finally {
                    StreamUtils.close(writer);
                }
            }
            @Override
            public String getContentType() {
                return ContentType.EXCEL.getMimeType();
            }
        };

        return rStream;
    }

    private Asset createExampleAsset(AssetType assetType) {
        Asset example = new Asset();

        example.setIdentifier(new FIDLabelModel("example.asset.identifier").getObject());
        example.setRfidNumber(new FIDLabelModel("example.asset.rfidNumber").getObject());
        example.setCustomerRefNumber(new FIDLabelModel("example.asset.customerRefNumber").getObject());
        example.setOwner(getCurrentUser().getOwner());

        if (getSessionUserOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.AdvancedLocation)) {
            PredefinedLocation location1 = new PredefinedLocation();
            location1.setName("Location 1");
            PredefinedLocation location2 = new PredefinedLocation();
            location2.setName("Location 2");
            location2.setParent(location1);
            PredefinedLocation location3 = new PredefinedLocation();
            location3.setName("Location 3");
            location3.setParent(location2);
            example.setAdvancedLocation(new Location(location3, new FIDLabelModel("label.freeform_location").getObject()));
        } else {
            example.setAdvancedLocation(Location.onlyFreeformLocation(new FIDLabelModel("example.asset.location").getObject()));
        }

        example.setPurchaseOrder(new FIDLabelModel("example.asset.purchaseOrder").getObject());
        example.setComments(new FIDLabelModel("example.asset.comments").getObject());
        example.setIdentified(new Date());

        List<AssetStatus> statuses = getLoaderFactory().createAssetStatusListLoader().load();

        AssetStatus exampleStatus =  (statuses.isEmpty()) ? null : statuses.get(0);
        if (exampleStatus != null) {
            example.setAssetStatus(exampleStatus);
        }

        InfoOptionBean exampleOption;
        for (InfoFieldBean field: assetType.getInfoFields()) {
            exampleOption = new InfoOptionBean();
            exampleOption.setInfoField(field);
            exampleOption.setStaticData(false);
            exampleOption.setName("");
            example.getInfoOptions().add(exampleOption);
        }

        return example;
    }

    private String getExportFileName(AssetType assetType) {
        return ContentType.EXCEL.prepareFileName(new FIDLabelModel("label.export_file.asset",
                ArrayUtils.newArray(assetType.getName())).getObject());
    }

    public ImportResultStatus doImport(InputStream importDoc, AssetType type) {
        if (importDoc == null) {
            //TODO handle missing input file, maybe disable import button?
            return new ImportResultStatus(false, null,
                    new FIDLabelModel("error.file_required").getObject(), null);
        }
        else {
            return runImport(importDoc, type, new ImportTaskRegistry());
        }
    }

    /**
     *
     * @param importDoc
     * @param type
     * @return a tuple containing
     *  1) boolean - success/failure,
     *  2) list of validation failures (if any),
     *  3) Error message if import failed
     */
    private ImportResultStatus runImport(InputStream importDoc, AssetType type, ImportTaskRegistry taskRegistry) {
        String taskId = null;
        try {
            System.out.println("RunImport starting");
            // This case shouldn't happen since the form should not allow you to submit when one is already registered
            if (isImportRunning(taskRegistry)) {
                System.out.println("Import already running");
                return new ImportResultStatus(false, null,
                        new FIDLabelModel("error.import_already_running").getObject(), null);
            } else {
                taskRegistry.remove(getImportTaskId());
                getWebSessionMap().clearImportTaskId();
            }

            Importer importer = createAndValidateImporter(importDoc, type);
            List<ValidationResult> failedImportValidationResults = importer.readAndValidate();

            if (!failedImportValidationResults.isEmpty()) {
                System.out.println("Import validation failed");
                return new ImportResultStatus(false, failedImportValidationResults,
                        new FIDLabelModel("label.validation_failed").getObject(),null);
            }
            taskId = executeImportTask(importer, type, taskRegistry);
        } catch (EmptyDocumentException e) {
            System.out.println("Import EmptyDocumentException");
            return new ImportResultStatus(false, null,
                    new FIDLabelModel("error.empty_import_document").getObject(), null);
        } catch (InvalidTitleException e) {
            System.out.println("Import InvalidTitleException");
            return new ImportResultStatus(false, null,
                    new FIDLabelModel("error.bad_file_format", ArrayUtils.newArray(e.getTitle())).getObject(), null);
        } catch (Exception e) {
            // if the file is not an excel file, the exception that comes back will be a BifException contained inside an IOException
            if (e.getCause() instanceof BiffException) {
                logger.warn(String.format("Import failed for User [%s]", getCurrentUser().toString()), e.getCause());
                System.out.println("Import BiffException");
                return new ImportResultStatus(false, null,
                        new FIDLabelModel("error.unsupported_content_type").getObject(), null);
            } else {
                // we don't know exactly what happened here, log it and fail generically
                System.out.println("Import generic failure");
                logger.error(String.format("Import failed for User [%s]", getCurrentUser().toString()), e);
                return new ImportResultStatus(false, null, new FIDLabelModel("error.import_failed").getObject(), null);
            }
        }
        System.out.println("Import successfully submitted");
        return new ImportResultStatus(true, null, null, taskId); // SUCCESS;
    }

    private Importer createAndValidateImporter(InputStream importDoc, AssetType type)
            throws IOException, ParseException, MarshalingException {

        MapReader mapReader = new ExcelXSSFMapReader(importDoc, getSessionUser().getTimeZone());
        Importer importer = createImporter(mapReader, type);
        return importer;
    }

    private String executeImportTask(Importer importer, AssetType type, ImportTaskRegistry taskRegistry) {
        ImportTask task = new ImportTask(importer, createSuccessNotification(type), createFailureNotification(type));

        TaskExecutor.getInstance().execute(task);

        // don't register the task and id until after we've sent
        // to the executor, in case it was rejected
        String taskId = task.getId();
        setImportTaskId(taskId);
        taskRegistry.register(task);
        return taskId;
    }

    private Importer createImporter(MapReader reader, AssetType type) {
        return getImporterFactory().createAssetImporter(reader, getCurrentUser(), type);
    }

    protected ImporterFactory getImporterFactory() {
        return new ImporterFactory(getSecurityFilter());
    }

    private boolean isImportRunning(ImportTaskRegistry taskRegistry) {
        return (getImportTaskId() != null && !(taskRegistry.get(getImportTaskId())).isCompleted());
    }

    private String getImportTaskId() {
        return getWebSessionMap().getImportTaskId();
    }

    private void setImportTaskId(String taskId) {
        getWebSessionMap().setImportTaskId(taskId);
    }

    private ImportSuccessNotification createSuccessNotification(AssetType type) {
        return new AssetImportSuccessNotification(getCurrentUser(), type);
    }

    private ImportFailureNotification createFailureNotification(AssetType type) {
        return new AssetImportFailureNotification(getCurrentUser(), type);
    }

    private BaseOrg getSessionUserOwner() {
        return getSessionUser().getOwner();
    }

    public LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }

    public WebSessionMap getWebSessionMap() {
        return webSessionMapModel.getObject();
    }

    private User getCurrentUser() {
        return currentUserModel.getObject();
    }
    private SessionUser getSessionUser() {
        return sessionUserModel.getObject();
    }
    private SecurityFilter getSecurityFilter() {
        return securityFilterModel.getObject();
    }
}
