package com.n4systems.fieldid.wicket.pages;

import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.ImporterFactory;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.widgets.DownloadExportNotificationPage;
import com.n4systems.fieldid.wicket.pages.widgets.EntityImportInitiator;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultPage;
import com.n4systems.fieldid.wicket.pages.widgets.ImportResultStatus;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadCoordinator;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import rfid.web.helper.SessionUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Group import/export boilerplate code in this class to reduce code repetition.
 */
abstract public class BaseImportExportPanel extends Panel {

    private static final Logger logger = Logger.getLogger(BaseImportExportPanel.class);

    private static final String COLORBOX_CLASS = "colorboxLink";
    private static final String JQUERY_COLORBOX_CMD = "jQuery('."+COLORBOX_CLASS+"')" +
        ".colorbox({maxHeight: '600px', width: '600px', height:'360px', ajax: true, iframe: true});";

    private IModel<User> currentUserModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<WebSessionMap> webSessionMapModel;
    private IModel<DownloadLink> downloadLinkModel;
    private LoaderFactory loaderFactory;
    protected WebMarkupContainer loadingWheel;

    public BaseImportExportPanel(String id, IModel<User> currentUserModel,
                                 IModel<SessionUser> sessionUserModel,
                                 IModel<SecurityFilter> securityFilterModel,
                                 IModel<WebSessionMap> webSessionMapModel) {
        super(id);
        this.currentUserModel = currentUserModel;
        this.sessionUserModel = sessionUserModel;
        this.securityFilterModel = securityFilterModel;
        this.webSessionMapModel = webSessionMapModel;
        downloadLinkModel = new Model<DownloadLink>().of((DownloadLink) null);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSS(".busyIndicator {display: block; position: fixed; left: 50%; top: 50%; opacity : 0.8;}", null);
        response.renderCSS(".hideElement {display: none;}", null);
        response.renderOnDomReadyJavaScript(JQUERY_COLORBOX_CMD);
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

    /**
     * Call this method to add the busy indicator to the page
     * @param parent
     * @return
     */
    protected Component addBusyIndicator(MarkupContainer parent) {
        loadingWheel = new WebMarkupContainer("loadingWheel");
        loadingWheel.setOutputMarkupId(true);
        loadingWheel.setOutputMarkupPlaceholderTag(true);
        parent.add(loadingWheel);
        return loadingWheel;
    }

    /**
     * Call this method to export a template (example) file
     * @return
     */
    protected AbstractResourceStreamWriter performTemplateExport() {

        AbstractResourceStreamWriter rStream = new AbstractResourceStreamWriter() {
            @Override
            public void write(Response output) {
                MapWriter writer = null;
                try {
                    writer = new ExcelXSSFMapWriter(new DateTimeDefiner(getCurrentUser()));
                    getTemplateExporter().export(writer);
                    ((ExcelXSSFMapWriter)writer).writeToStream(getResponse().getOutputStream());

                } catch (Exception e) {
                    logger.error(getTemplateExportFailureMessage(), e);
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

    /**
     * Create the exporter which creates the template data
     * @return
     */
    abstract protected Exporter getTemplateExporter();

    /**
     * Message to write to the log in case the template export fails
     * @return
     */
    abstract protected String getTemplateExportFailureMessage();

    /**
     * Call this method to perform an export of the data provided in the implementation of method 'getDataDownloadLink'
     */
    protected void performDataExport() {
        try {
            DownloadLink downloadLink = getDataDownloadLink();
            downloadLinkModel.setObject(downloadLink);
            DownloadExportNotificationPage downloadWindow = new DownloadExportNotificationPage(
                    downloadLinkModel);
            setResponsePage(downloadWindow);
        } catch (RuntimeException e) {
            logger.error(getDataDownloadLoggerErrorText(), e);
            throw new RestartResponseException(OopsPage.class,
                    PageParametersBuilder.param(OopsPage.PARAM_ERROR_TYPE_KEY, getDataDownloadOopsErrorText()));
        }
    }

    abstract protected DownloadLink getDataDownloadLink();

    /**
     * Error message to be written to the log if the data export fails.
     * @return
     */
    abstract protected String getDataDownloadLoggerErrorText();

    /**
     * Error message to be displayed in the OOPS page if the data export fails.
     * @return
     */
    abstract protected String getDataDownloadOopsErrorText();

    /**
     * Call this method to add an upload button using the provided wicket id.
     * @param id
     * @return
     */
    protected AjaxButton addAjaxUploadButton(String id, final FileUploadField fileUploadField) {
        AjaxButton fileUploadButton = new AjaxButton(id) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                ImportResultStatus result;
                FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    try {
                        logger.info("Beginning import of file " + fileUpload.getClientFileName());
                        InputStream inputStream = fileUpload.getInputStream();
                        EntityImportInitiator importService = new EntityImportInitiator(getWebSessionMap(),
                                getCurrentUser(), getSessionUser(), getSecurityFilter()) {
                            @Override
                            protected ImportSuccessNotification createSuccessNotification() {
                                return getImportSuccessNotification();
                            }

                            @Override
                            protected ImportFailureNotification createFailureNotification() {
                                return getImportFailureNotification();
                            }

                            @Override
                            protected Importer createImporter(MapReader reader) {
                                return getImporter(reader, getImporterFactory());
                            }
                        };
                        result = importService.doImport(inputStream);
                    } catch (IOException ex) {
                        logger.error("Exception reading input file", ex);
                        result = new ImportResultStatus(false, null,
                                new FIDLabelModel("error.io_error_reading_import_file").getObject(), null);
                    } finally {
                        logger.info("Import validation phase completed");
                    }
                } else {
                    logger.error("Input file required");
                    result = new ImportResultStatus(false, null,
                            new FIDLabelModel("error.file_required").getObject(), null);
                }
                ImportResultPage resultPage = new ImportResultPage(result) {

                    @Override
                    protected PageParameters getRerunParameters() {
                        return getParametersForImportRerun();
                    }

                    @Override
                    protected Class<? extends IRequestablePage> getRerunPageClass() {
                        return getImportRerunPageClass();
                    }
                };
                setResponsePage(resultPage);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxCallDecorator() {
                    @Override
                    public CharSequence decorateScript(Component component, CharSequence script) {
                        /* When this button is clicked show the busy indicator */
                        return "document.getElementById('" + loadingWheel.getMarkupId() + "').className='busyIndicator';" + script;
                    }
                };
            }
        };
        return fileUploadButton;
    }

    abstract protected ImportSuccessNotification getImportSuccessNotification();

    abstract protected ImportFailureNotification getImportFailureNotification();

    abstract protected Importer getImporter(MapReader reader, ImporterFactory importerFactory);

    abstract protected Class<? extends IRequestablePage> getImportRerunPageClass();

    abstract protected PageParameters getParametersForImportRerun();


    protected DownloadCoordinator getDownloadCoordinator() {
        DownloadCoordinator downloadCoordinator = new DownloadCoordinator(getCurrentUser(),
                new SaverFactory().createDownloadLinkSaver());
        return downloadCoordinator;
    }

    protected String getDownloadLinkUrl() {
        return new ActionURLBuilder(getBaseURI(), getConfigContext()).setAction("showDownloads").build() + "?fileId=";
    }

    protected ConfigurationProvider getConfigContext() {
        return ConfigService.getInstance();
    }

    protected URI getBaseURI() {
        // creates a URI based on the current url, and resolved against the context path which should be /fieldid.
        // We add on the extra / since we currently need it.
        String fullPath = getRequestCycle().getUrlRenderer().renderFullUrl(Url.parse(getRequest().getContextPath()));
        return URI.create(fullPath + "/");
    }

    protected User getCurrentUser() {
        return currentUserModel.getObject();
    }

    protected String getText(String key) {
        return new FIDLabelModel(key).getObject();
    }

    protected SessionUser getSessionUser() {
        return sessionUserModel.getObject();
    }

    protected SecurityFilter getSecurityFilter() {
        return securityFilterModel.getObject();
    }

    protected WebSessionMap getWebSessionMap() {
        return webSessionMapModel.getObject();
    }

    protected LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }
}
