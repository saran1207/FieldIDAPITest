package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.exporting.ExportException;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.ImporterFactory;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.BaseImportExportPanel;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.notifications.CustomerImportFailureNotification;
import com.n4systems.notifiers.notifications.CustomerImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import rfid.web.helper.SessionUser;

import java.io.IOException;


/**
 * Panel to handle export of customer template and existing customer data.
 */
public class CustomerImportPanel extends BaseImportExportPanel {

    private static final Logger logger = Logger.getLogger(CustomerImportPanel.class);


    public CustomerImportPanel(String id, IModel<User> currentUserModel,
                               IModel<SessionUser> sessionUserModel,
                               IModel<SecurityFilter> securityFilterModel,
                               IModel<WebSessionMap> webSessionMapModel) {
        super(id, currentUserModel, sessionUserModel, securityFilterModel, webSessionMapModel);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/import.css");
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
                AbstractResourceStreamWriter rStream = performTemplateExport();
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, getTemplateExportFileName());
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };
        add(downloadTemplateLink);

        // Add file upload section
        final FileUploadField fileUploadField = new FileUploadField("fileToUpload");

        Form fileUploadForm = new Form("fileUploadForm");

        AjaxButton fileUploadButton = addAjaxUploadButton("fileUploadSubmit", fileUploadField);

       /* AjaxButton fileUploadButton =new AjaxButton("fileUploadSubmit") {
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
                                return new CustomerImportSuccessNotification(getCurrentUser(), getText("label.customer"));
                            }
                            @Override
                            protected ImportFailureNotification createFailureNotification() {
                                return new CustomerImportFailureNotification(getCurrentUser(), getText("label.customer"));
                            }
                            @Override
                            protected Importer createImporter(MapReader reader) {
                                return getImporterFactory().createCustomerImporter(reader);
                            }
                        };
                        result = importService.doImport(inputStream);
                    } catch (IOException ex) {
                        logger.error("Exception reading input file", ex);
                        result = new ImportResultStatus(false, null,
                                new FIDLabelModel("error.io_error_reading_import_file").getObject(), null);
                    }
                    finally {
                        logger.info("Import validation phase completed");
                    }
                }
                else {
                    logger.error("Input file required");
                    result = new ImportResultStatus(false, null,
                            new FIDLabelModel("error.file_required").getObject(), null);
                }
                ImportResultPage resultPage = new ImportResultPage(result) {

                    @Override
                    protected PageParameters getRerunParameters() {
                        return new PageParameters();
                    }
                    @Override
                    protected Class<? extends IRequestablePage> getRerunPageClass() {
                        return CustomerImportPage.class;
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
                        *//* When this button is clicked show the busy indicator *//*
                        return "document.getElementById('"+ loadingWheel.getMarkupId()+"').className='busyIndicator';"+script;
                    }
                };
            }
        };*/

        fileUploadForm.add(fileUploadButton);
        fileUploadForm.setMultiPart(true);
        fileUploadForm.add(fileUploadField);
        add(fileUploadForm);

        addBusyIndicator(this);
    }

    @Override
    protected ImportSuccessNotification getImportSuccessNotification() {
        return new CustomerImportSuccessNotification(getCurrentUser(), getText("label.customer"));
    }

    @Override
    protected ImportFailureNotification getImportFailureNotification() {
        return new CustomerImportFailureNotification(getCurrentUser(), getText("label.customer"));
    }

    @Override
    protected Importer getImporter(MapReader reader, ImporterFactory importerFactory) {
        return importerFactory.createCustomerImporter(reader);
    }

    @Override
    protected Class<? extends IRequestablePage> getImportRerunPageClass() {
        return CustomerImportPage.class;
    }

    @Override
    protected PageParameters getParametersForImportRerun() {
        return new PageParameters();
    }

    @Override
    protected Exporter getTemplateExporter() {
        Exporter exporter = new Exporter() {

            @Override
            public void export(MapWriter mapWriter) throws ExportException {
                ExportMapMarshaller<FullExternalOrgView> marshaler = new ExportMapMarshaller<FullExternalOrgView>(FullExternalOrgView.class);
                try {
                    mapWriter.write(marshaler.toBeanMap(createExampleCustomer()));
                    mapWriter.write(marshaler.toBeanMap(createExampleDivision()));
                }
                catch(MarshalingException | IOException ex){
                    throw new ExportException(ex);
                }
            }
        };
        return exporter;
    }

    @Override
    protected String getTemplateExportFailureMessage() {
        return "Failed generating example customer export";
    }

    @Override
    protected DownloadLink getDataDownloadLink() {
        String downloadFileName = getText("label.export_file.customer");
        DownloadLink downloadLink = getDownloadCoordinator().generateCustomerExport(downloadFileName,
                getDownloadLinkUrl(), createCustomerOrgListLoader(), getSecurityFilter());
        return downloadLink;
    }

    @Override
    protected String getDataDownloadLoggerErrorText() {
        return "Unable to execute Customer data export";
    }

    @Override
    protected String getDataDownloadOopsErrorText() {
        return new FIDLabelModel("error.export_failed.customer").getObject();
    }

    private CustomerOrgListLoader createCustomerOrgListLoader() {
        return getLoaderFactory().createCustomerOrgListLoader().withoutLinkedOrgs().setPostFetchFields("modifiedBy", "createdBy");
    }

    private FullExternalOrgView createExampleCustomer() {
        FullExternalOrgView view = new FullExternalOrgView();
        view.setTypeToCustomer();

        view.setName(getText("example.customer.name"));
        view.setCode(getText("example.customer.code"));
        view.setParentOrg(getCurrentUser().getOwner().getName());
        view.setContactName(getText("example.customer.contact.name"));
        view.setContactEmail(getText("example.customer.contact.email"));
        view.setStreetAddress(getText("example.customer.addr"));
        view.setCity(getText("example.customer.city"));
        view.setState(getText("example.customer.state"));
        view.setCountry(getText("example.customer.country"));
        view.setZip(getText("example.customer.zip"));
        view.setPhone1(getText("example.customer.phone1"));
        view.setPhone2(getText("example.customer.phone2"));
        view.setFax1(getText("example.customer.fax"));
        view.setNotes(getText("example.customer.notes"));

        return view;
    }

    private FullExternalOrgView createExampleDivision() {
        FullExternalOrgView view = new FullExternalOrgView();
        view.setTypeToDivision();

        view.setName(getText("example.division.name"));
        view.setCode(getText("example.division.code"));
        view.setContactName(getText("example.division.contact.name"));
        view.setContactEmail(getText("example.division.contact.email"));
        view.setStreetAddress(getText("example.division.addr"));
        view.setCity(getText("example.division.city"));
        view.setState(getText("example.division.state"));
        view.setCountry(getText("example.division.country"));
        view.setZip(getText("example.division.zip"));
        view.setPhone1(getText("example.division.phone1"));
        view.setPhone2(getText("example.division.phone2"));
        view.setFax1(getText("example.division.fax"));
        view.setNotes(getText("example.division.notes"));

        return view;
    }

    private String getTemplateExportFileName() {
        return ContentType.EXCEL.prepareFileName(getText("label.export_file.customer"));
    }



}
