package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.BrandingLogoFormPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.image.FileSystemBrandingLogoFileProcessor;
import com.n4systems.util.persistence.image.UploadedImage;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

//import com.n4systems.fieldid.wicket.components.org.InternalOrgFormDetailsPanel;

public class BrandingPage extends FieldIDTemplatePage {

    @SpringBean
    protected OrgService orgService;
    @SpringBean
    protected S3Service s3Service;

    private Long uniqueId;
    protected IModel<InternalOrg> internalOrg;
    protected Country country;
    protected Region region;
    protected InternalOrg organization;

    public BrandingPage(IModel<InternalOrg> internalOrg) {
        this.uniqueId = internalOrg.getObject().getId();
        this.internalOrg = internalOrg;
    }

    public BrandingPage (){
        PrimaryOrg primaryOrg = getPrimaryOrg();
        organization = primaryOrg;
        internalOrg = Model.of(organization);
    }

    protected void doSave() {
        update();
        setResponsePage(SettingsPage.class);
    }

    protected BrandingLogoFormPanel reportImagePanel;

    protected UploadedImage getBrandingLogo() {
        File reportImage;
        if (internalOrg.getObject().isPrimary()) reportImage = PathHandler.getBrandingLogo((PrimaryOrg) internalOrg.getObject());
        else reportImage = new File("");
        UploadedImage uploadedImage = new UploadedImage();

        if (reportImage.exists()) {
            uploadedImage.setImage(reportImage);
            uploadedImage.setUploadDirectory(reportImage.getPath());
        } else if(s3Service.isBrandingLogoExists(internalOrg.getObject().getId(), internalOrg.getObject().isPrimary())){
            reportImage = s3Service.downloadBrandingLogoImage(internalOrg.getObject());
            uploadedImage.setImage(reportImage);
            uploadedImage.setUploadDirectory(reportImage.getPath());
        }

        return uploadedImage;
    }

    protected void saveBrandingLogoImageFile(UploadedImage reportImage) {
        new FileSystemBrandingLogoFileProcessor(internalOrg.getObject()).process(reportImage);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddInternalOrgForm("addInternalOrgForm"));
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

    class AddInternalOrgForm extends Form {

        SubmitLink submitLink;

        public AddInternalOrgForm(String id) {
            super(id);

            add(reportImagePanel = new BrandingLogoFormPanel("brandingLogoPanel", internalOrg, getBrandingLogo()));

            add(new TextField<String>("internalOrgWebSite", new PropertyModel<String>(internalOrg, "webSite")));

            add(submitLink = new SubmitLink("save"));

            addConfirmBehavior(submitLink);

            add(new BookmarkablePageLink<BrandingPage>("cancel", BrandingPage.class));

            setOutputMarkupPlaceholderTag(true);
        }

        @Override
        protected void onSubmit() {
            doSave();
            FieldIDSession.get().info(new FIDLabelModel("message.organizationsaved").getObject());
        }
    }

    public PrimaryOrg getPrimaryOrg() {
        return getSecurityGuard().getPrimaryOrg();
    }

}


