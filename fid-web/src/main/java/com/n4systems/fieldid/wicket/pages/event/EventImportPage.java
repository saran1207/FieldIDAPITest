package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.api.model.EventView;
import com.n4systems.exporting.EventExporter;
import com.n4systems.exporting.beanutils.*;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.*;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.model.eventschedule.NextEventDateByEventPassthruLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ArrayUtils;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by agrabovskis on 2017-10-31.
 */
public class EventImportPage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(EventImportPage.class);

    private LoaderFactory loaderFactory;
    private IModel<ThingEventType> selectedEventType;

    public EventImportPage(PageParameters params) {
        super(params);
        selectedEventType = Model.of((ThingEventType) null);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //response.renderCSSReference("style/legacy/fieldid.css");
        response.renderCSSReference("style/legacy/pageStyles/import.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.import_events"));
    }

    private void addComponents() {

        final DropDownChoice<ThingEventType> eventTypeSelection = new DropDownChoice<ThingEventType>("eventTypesSelectionList",
                selectedEventType,
                new LoadableDetachableModel<List<ThingEventType>>() {
                    @Override
                    protected List<ThingEventType> load() {
                        QueryBuilder<ThingEventType> thingEventTypeQueryBuilder =
                                new QueryBuilder<ThingEventType>(ThingEventType.class, getSecurityFilter());
                        thingEventTypeQueryBuilder.addOrder("name");
                        return PersistenceManager.findAll(thingEventTypeQueryBuilder);
                    }
                },
                new IChoiceRenderer<ThingEventType>() {
                    @Override
                    public Object getDisplayValue(ThingEventType object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(ThingEventType object, int index) {
                        return object.getId().toString();
                    }
                }
        ) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

        };
        add(eventTypeSelection);

        Link downloadTemplateLink = new Link("downloadTemplateLink") {

            @Override
            public void onClick() {
                System.out.println("Download template onClick");
                ThingEventType eventType = getPopulatedEventType(selectedEventType.getObject());
                if (eventType != null) {
                    System.out.println("... selected event type " + eventType.getName());
                    AbstractResourceStreamWriter rStream = doDownloadExample(eventType, true);
                    System.out.println("... of type " + rStream.getContentType());
                    ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(eventType));
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                }
            }
        };
        add(downloadTemplateLink);

        Link downloadMinimalTemplateLink = new Link("downloadMinimalTemplateLink") {

            @Override
            public void onClick() {
                System.out.println("Download minimal template onClick");
                ThingEventType eventType = getPopulatedEventType(selectedEventType.getObject());
                if (eventType != null) {
                    System.out.println("... selected event type " + eventType.getName());
                    AbstractResourceStreamWriter rStream = doDownloadExample(eventType, false);
                    System.out.println("... of type " + rStream.getContentType());
                    ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(eventType));
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                }
            }
        };
        add(downloadMinimalTemplateLink);

        final FileUploadField fileUploadField = new FileUploadField("fileToUpload");
        Form fileUploadForm = new Form("fileUploadForm") {
            @Override
            protected void onSubmit() {
                System.out.println("File upload submitted");
                /*ImportResultStatus result;
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
                setResponsePage(resultPage);*/
            }
        };
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);
        add(fileUploadForm);
    }

    private ThingEventType getPopulatedEventType(ThingEventType type ) {
        if (type != null)
            return getLoaderFactory().createFilteredIdLoader(ThingEventType.class).setPostFetchFields("eventForm.sections").setId(type.getId()).load();
        else
            return null;
    }

    public AbstractResourceStreamWriter doDownloadExample(ThingEventType type, boolean isIncludeRecommendationsAndDeficiencies) {

        try {
            ListLoader<ThingEvent> eventLoader = getLoaderFactory().createPassthruListLoader(
                    Collections.singletonList(createExampleEvent(type)));
            NextEventDateByEventLoader nextDateLoader = new NextEventDateByEventPassthruLoader(createExampleNextDate());

            // override the serialization handler factory so we can control which fields are output.  (i.e. use different handlers
            //  depending on the "isInclude..." state of the action.
            SerializationHandlerFactory handlerFactory = new SerializationHandlerFactory() {
                @Override
                protected Class<? extends SerializationHandler> getSerializationHandlerForField(Field field, SerializableField annotation) {
                    if (annotation.handler().equals(CriteriaResultSerializationHandler.class) && !isIncludeRecommendationsAndDeficiencies) {
                        return FilteredCriteriaResultSerializationHandler.class;
                    }
                    return super.getSerializationHandlerForField(field, annotation);
                }
            };
            EventExporter exporter = new EventExporter(eventLoader, nextDateLoader, new ExportMapMarshaller<>(EventView.class, handlerFactory));
            AbstractResourceStreamWriter rStream = new AbstractResourceStreamWriter() {
                @Override
                public void write(Response output) {
                    MapWriter writer = null;
                    try {
                        writer = new ExcelXSSFMapWriter(new DateTimeDefiner(getCurrentUser()));
                        exporter.export(writer);
                        ((ExcelXSSFMapWriter) writer).writeToStream(getResponse().getOutputStream());

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
        catch(Exception ex) {
            logger.error("Attempt to create event download example exel file failed", ex);
            throw new RuntimeException(ex);
        }
    }

    private ThingEvent createExampleEvent(ThingEventType type) throws InstantiationException, IllegalAccessException {
        ThingEvent example = new ThingEvent();

        example.setEventForm(type.getEventForm());

        example.setDate(new Date());
        example.setOwner(getSessionUser().getOwner());
        example.setPrintable(true);
        example.setPerformedBy(getCurrentUser());
        example.setEventResult(EventResult.PASS);

        example.setComments(new FIDLabelModel("example.event.comments").getObject());
        example.setAdvancedLocation(Location.onlyFreeformLocation(new FIDLabelModel("example.event.location").getObject()));

        example.setBook(new EventBook());
        example.getBook().setName(new FIDLabelModel("example.event.book").getObject());

        example.setAsset(new Asset());
        example.getAsset().setIdentifier(new FIDLabelModel("example.event.identifier").getObject());

        example.setAssetStatus(getExampleAssetStatus());

        example.setCriteriaResults(createExampleResults(type));

        return example;
    }
    private Date createExampleNextDate() {
        return DateHelper.addDaysToDate(new Date(), 365L);
    }

    private AssetStatus getExampleAssetStatus() {
        List<AssetStatus> statuses = getLoaderFactory().createAssetStatusListLoader().load();

        return (statuses.isEmpty()) ? null : statuses.get(0);
    }
    private Set<CriteriaResult> createExampleResults(ThingEventType type) throws InstantiationException, IllegalAccessException {
        Set<CriteriaResult> results = new HashSet<CriteriaResult>();

        if(type.getEventForm() != null) {
            List<CriteriaSection> availableSections = type.getEventForm().getAvailableSections();
            for (CriteriaSection section:availableSections) {
                for (Criteria criterion:section.getAvailableCriteria()) {
                    if (criterion.getCriteriaType() != CriteriaType.SIGNATURE) {		// skip signature because we don't support import/export of those fields.
                        Class<? extends CriteriaResult> resultClass = criterion.getCriteriaType().getResultClass();
                        CriteriaResult result = resultClass.newInstance();
                        result.setCriteria(criterion);
                        result.setDeficiencies(new ArrayList<>());
                        result.setRecommendations(new ArrayList<>());
                        result.setTenant(criterion.getTenant());
                        results.add(result);
                    }
                }
            }
        }
        return results;
    }

    private String getExportFileName(ThingEventType eventType) {
        return ContentType.EXCEL.prepareFileName(new FIDLabelModel("label.export_file.event",
                ArrayUtils.newArray(eventType.getName())).getObject());
    }

    /*public ImportResultStatus doImport(InputStream importDoc, AssetType type) {
        if (importDoc == null) {
            //TODO handle missing input file, maybe disable import button?
            return new ImportResultStatus(false, null,
                    new FIDLabelModel("error.file_required").getObject(), null);
        }
        else {
            return runImport(importDoc, type, new ImportTaskRegistry());
        }
    }*/


    /**
     *
     * @param importDoc
     * @param type
     * @return a tuple containing
     *  1) boolean - success/failure,
     *  2) list of validation failures (if any),
     *  3) Error message if import failed
     */
   /* private ImportResultStatus runImport(InputStream importDoc, AssetType type, ImportTaskRegistry taskRegistry) {
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
    }*/

    //public WebSessionMap getWebSessionMap() {
    //    return webSessionMapModel.getObject();
    // }

    private LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }
}

