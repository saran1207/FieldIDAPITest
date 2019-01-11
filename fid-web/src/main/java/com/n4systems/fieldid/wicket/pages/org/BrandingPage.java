package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.BrandingLogoFormPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplateWithFeedbackPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.image.FileSystemBrandingLogoFileProcessor;
import com.n4systems.util.persistence.image.UploadedImage;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.IOException;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class BrandingPage extends FieldIDTemplateWithFeedbackPage {

    @SpringBean
    protected OrgService orgService;
    @SpringBean
    protected S3Service s3Service;
    private Long uniqueId;
    protected IModel<InternalOrg> internalOrg;
    protected InternalOrg organization;

    public BrandingPage (){
        super();
        organization = getPrimaryOrg();
        internalOrg = Model.of(organization);
    }

    public BrandingPage(PageParameters params) {
        super(params);
        organization = getPrimaryOrg();
        internalOrg = Model.of(organization);
    }

    protected void doSave() {
        update();
        setResponsePage(BrandingPage.class);
    }

    protected BrandingLogoFormPanel reportImagePanel;

    protected UploadedImage getBrandingLogo() {
        File reportImage = new File("");
        if (internalOrg.getObject().isPrimary()) reportImage = PathHandler.getBrandingLogo((PrimaryOrg) internalOrg.getObject());
        UploadedImage uploadedImage = new UploadedImage();

        try {
            if (reportImage.exists()) {
                uploadedImage.setImage(reportImage);
                uploadedImage.setUploadDirectory(reportImage.getPath());
            } else if (s3Service.isBrandingLogoExists(internalOrg.getObject().getId(), internalOrg.getObject().isPrimary())) {
                reportImage = s3Service.downloadBrandingLogoImage(internalOrg.getObject());
                uploadedImage.setImage(reportImage);
                uploadedImage.setUploadDirectory(reportImage.getPath());
            }
        }
        catch(IOException|FileProcessingException e) {
            Session.get().error("Internal Error during branding logo processing");
        }

        return uploadedImage;
    }

    protected void saveBrandingLogoImageFile(UploadedImage reportImage) {
        try {
            new FileSystemBrandingLogoFileProcessor(internalOrg.getObject()).process(reportImage);
        }
        catch(IOException|FileProcessingException e) {
            Session.get().error("Internal Error during branding logo processing");
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new EditInternalOrgForm("editInternalOrgForm"));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_branding.plural"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, SettingsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.logo_and_website")).page(BrandingPage.class).build()
        ));
    }

    protected InternalOrg update() {
        InternalOrg org = internalOrg.getObject();

        UploadedImage internalOrgLogoImage = reportImagePanel.getUploadedImage();

        if(internalOrgLogoImage.isNewImage() || internalOrgLogoImage.isRemoveImage()) {
            saveBrandingLogoImageFile(internalOrgLogoImage);
        }

        if(org.isArchived()) {
            org.activateEntity();
        }

        orgService.update(org);

        return org;
    }

    protected void addConfirmBehavior(SubmitLink submitLink) {}

    class EditInternalOrgForm extends Form {

        SubmitLink submitLink;

        public EditInternalOrgForm(String id) {
            super(id);

            add(reportImagePanel = new BrandingLogoFormPanel("brandingLogoPanel", internalOrg, getBrandingLogo(), getFeedbackPanel()));

            add(new TextField<String>("internalOrgWebSite", new PropertyModel<String>(internalOrg, "webSite")));

            add(submitLink = new SubmitLink("save"));

            addConfirmBehavior(submitLink);

            add(new BookmarkablePageLink<BrandingPage>("cancel", BrandingPage.class));

            setOutputMarkupPlaceholderTag(true);

            setOutputMarkupId(true);
        }

        @Override
        protected void onSubmit() {
            doSave();
            FieldIDSession.get().info(new FIDLabelModel("message.system_settings_updated").getObject());
        }
    }

    public PrimaryOrg getPrimaryOrg() {
        return getSecurityGuard().getPrimaryOrg();
    }

}


