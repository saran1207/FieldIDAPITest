package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.exporting.ExportException;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.widgets.DownloadExportNotificationPage;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadCoordinator;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.log4j.Logger;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

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
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<DownloadLink> downloadLinkModel;
    private LoaderFactory loaderFactory;

    public BaseImportExportPanel(String id, IModel<User> currentUserModel, IModel<SecurityFilter> securityFilterModel) {
        super(id);
        this.currentUserModel = currentUserModel;
        this.securityFilterModel = securityFilterModel;
        downloadLinkModel = new Model<DownloadLink>().of((DownloadLink) null);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
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

    abstract DownloadLink getDataDownloadLink();

    /**
     * Error message to be written to the log if the data export fails.
     * @return
     */
    abstract String getDataDownloadLoggerErrorText();

    /**
     * Error message to be displayed in the OOPS page if the data export fails.
     * @return
     */
    abstract String getDataDownloadOopsErrorText();

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

    protected SecurityFilter getSecurityFilter() {
        return securityFilterModel.getObject();
    }

    protected LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }
}
