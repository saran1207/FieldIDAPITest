package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.exporting.ExportException;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import java.io.IOException;


/**
 * Panel to handle export of customer template and existing customer data.
 */
public class CustomerImportPanel extends BaseImportExportPanel {

    private static final Logger logger = Logger.getLogger(CustomerImportPanel.class);

    public CustomerImportPanel(String id, IModel<User> currentUserModel, IModel<SecurityFilter> securityFilterModel) {
        super(id, currentUserModel, securityFilterModel);
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
