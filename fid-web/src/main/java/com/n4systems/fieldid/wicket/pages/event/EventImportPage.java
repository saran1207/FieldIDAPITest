package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.api.model.EventView;
import com.n4systems.exporting.EventExporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.beanutils.*;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.widgets.EntityImportInitiator;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultPage;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultStatus;
import com.n4systems.model.*;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.model.eventschedule.NextEventDateByEventPassthruLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.EventImportFailureNotification;
import com.n4systems.notifiers.notifications.EventImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import com.n4systems.util.ArrayUtils;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.string.StringValue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;


@UserPermissionFilter(userRequiresOneOf={Permissions.CREATE_EVENT})
public class EventImportPage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(EventImportPage.class);

    private LoaderFactory loaderFactory;
    private IModel<ThingEventType> selectedEventType;

    public EventImportPage(PageParameters params) {
        super(params);
        StringValue uniqueId = params.get("uniqueID");
        if (uniqueId != null && !uniqueId.isEmpty())
            selectedEventType = Model.of(getThingEventType(uniqueId.toLong()));
        else
            selectedEventType = Model.of((ThingEventType) null);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/import.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.import_events"));
    }

    private ThingEventType getSelectedEventType() {
        return selectedEventType.getObject();
    }

    private ThingEventType getPopulatedSelectedEventType() {
        return getPopulatedEventType(getSelectedEventType());
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
                        List<ThingEventType> types = PersistenceManager.findAll(thingEventTypeQueryBuilder);
                        if (selectedEventType.getObject() == null && types.size() > 0)
                            selectedEventType.setObject(types.get(0));
                        return types;
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
                ThingEventType eventType = getPopulatedEventType(selectedEventType.getObject());
                AbstractResourceStreamWriter rStream = doDownloadExample(eventType, true);
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(eventType));
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };
        add(downloadTemplateLink);

        Link downloadMinimalTemplateLink = new Link("downloadMinimalTemplateLink") {

            @Override
            public void onClick() {
                ThingEventType eventType = getPopulatedEventType(selectedEventType.getObject());
                AbstractResourceStreamWriter rStream = doDownloadExample(eventType, false);
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(eventType));
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };
        add(downloadMinimalTemplateLink);

        final FileUploadField fileUploadField = new FileUploadField("fileToUpload");
        Form fileUploadForm = new Form("fileUploadForm") {
            @Override
            protected void onSubmit() {
                ImportResultStatus result;
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    try {
                        InputStream inputStream = fileUpload.getInputStream();
                        EntityImportInitiator importService = new EntityImportInitiator(getWebSessionMap(), getCurrentUser(), getSessionUser(), getSecurityFilter()) {
                            @Override
                            protected ImportSuccessNotification createSuccessNotification() {
                                return new EventImportSuccessNotification(getCurrentUser(), getPopulatedSelectedEventType());
                            }
                            @Override
                            protected ImportFailureNotification createFailureNotification() {
                                return new EventImportFailureNotification(getCurrentUser(), getPopulatedSelectedEventType());
                            }
                            @Override
                            protected Importer createImporter(MapReader reader) {
                                return getImporterFactory().createEventImporter(reader, getCurrentUser(), getPopulatedSelectedEventType());
                            }
                        };
                        result = importService.doImport(inputStream);
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
                Long selectedEventTypeId = getSelectedEventType().getId();
                ImportResultPage resultPage = new ImportResultPage(result) {

                    @Override
                    protected PageParameters getRerunParameters() {
                        return PageParametersBuilder.uniqueId(selectedEventTypeId);
                    }
                    @Override
                    protected Class<? extends IRequestablePage> getRerunPageClass() {
                        return EventImportPage.class;
                    }
                };
                setResponsePage(resultPage);
            }
        };
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);
        add(fileUploadForm);
    }

    private ThingEventType getThingEventType(Long id) {
        return getLoaderFactory().createFilteredIdLoader(ThingEventType.class).setId(id).load();
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
                        logger.error("Failed generating example event export", e);
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
            logger.error("Attempt to create event download example excel file failed", ex);
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

    private LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }

    @Override
    public WebSessionMap getWebSessionMap() {
        return new WebSessionMap(getServletRequest().getSession(false));
    }
}

