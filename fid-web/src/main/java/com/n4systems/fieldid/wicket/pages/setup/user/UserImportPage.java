package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.api.conversion.users.UserToViewConverter;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.validators.YNValidator;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.UserExporter;
import com.n4systems.exporting.beanutils.*;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.widgets.DownloadExportNotificationPage;
import com.n4systems.fieldid.wicket.pages.widgets.EntityImportInitiator;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultPage;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultStatus;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadCoordinator;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.notifiers.notifications.UserImportFailureNotification;
import com.n4systems.notifiers.notifications.UserImportSuccessNotification;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.security.PasswordComplexityChecker;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.UserWelcomeNotificationProducer;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collections;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
public class UserImportPage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(UserImportPage.class);
    private static final String COLORBOX_CLASS = "colorboxLink";
    private static final String JQUERY_COLORBOX_CMD = "jQuery('."+COLORBOX_CLASS+"')" +
            ".colorbox({maxHeight: '600px', width: '600px', height:'360px', ajax: true, iframe: true});";

    @SpringBean
    private UserService userService;

    private IModel<UserListFilterCriteria> filterCriteriaModel;
    private IModel<DownloadLink> downloadLinkModel;

    private LoaderFactory loaderFactory;

    public UserImportPage() {
        filterCriteriaModel = getUserListFilterCriteria();
        downloadLinkModel = new Model<DownloadLink>().of((DownloadLink) null);
        addComponents();
    }

     public UserImportPage(PageParameters params) {
        super(params);
        filterCriteriaModel = getUserListFilterCriteria();
        downloadLinkModel = new Model<DownloadLink>().of((DownloadLink) null);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/import.css");
        response.renderCSS(".busyIndicator {display: block; position: fixed; left: 50%; top: 50%; opacity : 0.8;}", null);
        response.renderCSS(".hideElement {display: none;}", null);
        response.renderOnDomReadyJavaScript(JQUERY_COLORBOX_CMD);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    private void addComponents() {

        Link downloadDataLink = new Link("downloadDataLink") {

             @Override
             public void onClick() {
                    performDataExport();
                }
        };
        setLinkToColorbox(downloadDataLink);
        add(downloadDataLink);

        Link downloadTemplateLink = new Link("downloadTemplateLink") {

         @Override
         public void onClick() {
             AbstractResourceStreamWriter rStream = doDownloadExample(false);
             ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getExportFileName(getCurrentUser()));
             getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
         };
         add(downloadTemplateLink);

        final FileUploadField fileUploadField;
        fileUploadField = new FileUploadField("fileToUpload");
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
                                return new UserImportSuccessNotification(getCurrentUser());
                            }

                            @Override
                            protected ImportFailureNotification createFailureNotification() {
                                return new UserImportFailureNotification(getCurrentUser());
                            }

                            @Override
                            protected Importer createImporter(MapReader reader) {
                                URI baseURI = URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
                                UserWelcomeNotificationProducer userWelcomeNotificationProducer = new UserWelcomeNotificationProducer(ServiceLocator.getDefaultNotifier(), new ActionURLBuilder(baseURI, ConfigService.getInstance()));

                                return getImporterFactory().createUserImporter(reader, userWelcomeNotificationProducer, getTenant().getSettings().getUserLimits(), getCurrentUser().getTimeZoneID(), ServiceLocator.getTenantSettingsService().getTenantSettings(getTenantId()).getPasswordPolicy());
                            }
                        };
                        result = importService.doImport(inputStream);
                    } catch (IOException ex) {
                        logger.error("Exception reading input file", ex);
                        result = new ImportResultStatus(false, null,
                                new FIDLabelModel("error.io_error_reading_import_file").getObject(), null);
                    }
                } else {
                    result = new ImportResultStatus(false, null,
                            new FIDLabelModel("error.file_required").getObject(), null);
                }
                Long selectedUserId = getCurrentUser().getId();
                ImportResultPage resultPage = new ImportResultPage(result) {

                    @Override
                    protected PageParameters getRerunParameters() {
                        return PageParametersBuilder.uniqueId(selectedUserId);
                    }

                    @Override
                    protected Class<? extends IRequestablePage> getRerunPageClass() {
                        return UserImportPage.class;
                    }
                };
                setResponsePage(resultPage);
            }
        };
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);

        add(fileUploadForm);

    }

    public AbstractResourceStreamWriter doDownloadExample( boolean isIncludeRecommendationsAndDeficiencies) {

        try {
            ListLoader<User> userLoader = getLoaderFactory().createPassthruListLoader(
                    Collections.singletonList(createExampleUser()));

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
            UserToViewConverter userToViewConverter = new UserToViewConverter();
            userToViewConverter.setYNStr(YNValidator.YNField.Y.toString());
            UserExporter exporter = new UserExporter(userLoader, new ExportMapMarshaller<>(UserView.class, handlerFactory), userToViewConverter);
            AbstractResourceStreamWriter rStream = new AbstractResourceStreamWriter() {
                @Override
                public void write(Response output) {
                    MapWriter writer = null;
                    try {
                        writer = new ExcelXSSFMapWriter(new DateTimeDefiner(getCurrentUser()));
                        exporter.export(writer);
                        ((ExcelXSSFMapWriter) writer).writeToStream(getResponse().getOutputStream());

                    } catch (Exception e) {
                        logger.error("Failed generating example user export", e);
                        throw new RuntimeException(e);
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
            logger.error("Attempt to create user download example excel file failed", ex);
            throw new RuntimeException(ex);
        }
    }

    protected User createExampleUser() {
        User user = new User();
        user.setEmailAddress(new FIDLabelModel("example.customer.contact.email").getObject());
        user.setFirstName(new FIDLabelModel("example.customer.first.name").getObject());
        user.setLastName(new FIDLabelModel("example.customer.last.name").getObject());
        user.setUserID(getCurrentUser().getUserID());
        user.assignPassword(PasswordComplexityChecker.createDefault().generateValidPassword());
        user.setOwner(getCurrentUser().getOwner());
        user.setUserType(UserType.FULL);

        return user;
    }

    protected Model<UserListFilterCriteria> getUserListFilterCriteria() {
        return Model.of(new UserListFilterCriteria(false));
    }

    /**
     * Call this method to perform an export of the data provided in the implementation of method 'getDataDownloadLink'
     */
    protected void performDataExport() {
        try {
            DownloadLink downloadLink = getDataDownloadLink();
            downloadLinkModel.setObject(downloadLink);
            DownloadExportNotificationPage downloadWindow = new DownloadExportNotificationPage(downloadLinkModel);
            setResponsePage(downloadWindow);
        } catch (RuntimeException e) {
            logger.error(getDataDownloadLoggerErrorText(), e);
            throw new RestartResponseException(OopsPage.class,
                    PageParametersBuilder.param(OopsPage.PARAM_ERROR_TYPE_KEY, new FIDLabelModel("error.export_failed.customer").getObject()));
        }
    }

    protected DownloadLink getDataDownloadLink() {
        String downloadFileName = new FIDLabelModel("label.export_file.user").getObject();
        DownloadLink downloadLink = getDownloadCoordinator().generateUserExport(downloadFileName,
                getDownloadLinkUrl(), createUserListLoader(), getSecurityFilter());
        return downloadLink;
    }

    protected DownloadCoordinator getDownloadCoordinator() {
        DownloadCoordinator downloadCoordinator = new DownloadCoordinator(getCurrentUser(),
                new SaverFactory().createDownloadLinkSaver());
        return downloadCoordinator;
    }

    protected String getDownloadLinkUrl() {
        URI baseURI = URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
        return new ActionURLBuilder(baseURI, ConfigService.getInstance()).setAction("showDownloads").build() + "?fileId=";
    }

    private ListLoader<User> createUserListLoader() {
        return getLoaderFactory().createUserListLoader();
    }


    protected String getDataDownloadLoggerErrorText() {
        return "Unable to execute User data export";
    }

    /**
     * Call this on the data download link so when it is clicked a jquery colorbox will appear
     * @param component
     * @return
     */
    protected AbstractLink setLinkToColorbox(AbstractLink component) {
        component.add(new AttributeAppender("class", new Model<>(COLORBOX_CLASS), " "));
        return component;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_users.plural"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        UserListFilterCriteria criteria = new UserListFilterCriteria(filterCriteriaModel.getObject());
        Long activeUserCount = userService.countUsers(criteria.withArchivedOnly(false));
        Long archivedUserCount = userService.countUsers(criteria.withArchivedOnly());
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeUserCount)).page(UsersListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedUserCount)).page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label(new FIDLabelModel("nav.import_export")).page(UserImportPage.class).onRight().build()
        ));
    }

    private String getExportFileName(User user) {
        return ContentType.EXCEL.prepareFileName(new FIDLabelModel("label.export_file.user").getObject());
    }

    private LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }

    public WebSessionMap getWebSessionMap() {
        return new WebSessionMap(getServletRequest().getSession(false));
    }

}
