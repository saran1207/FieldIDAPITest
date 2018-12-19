package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.SecondaryOrgFormAddressPanel;
import com.n4systems.fieldid.wicket.components.org.SecondaryOrgFormDetailsPanel;
import com.n4systems.fieldid.wicket.components.org.SecondaryOrgFormLocalizationPanel;
import com.n4systems.fieldid.wicket.components.org.SecondaryOrgFormReportImagePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.image.FileSystemSecondaryOrgReportFileProcessor;
import com.n4systems.util.persistence.image.UploadedImage;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class OrgPage extends FieldIDTemplatePage {

    @SpringBean
    protected UserService userService;
    @SpringBean
    protected OrgService orgService;

    protected UserType userType;
    private Long uniqueId;
    protected IModel<SecondaryOrg> secondaryOrgModel;
    protected IModel<OrgListFilterCriteria> filterCriteriaModel;
    protected Country country;
    protected Region region;

    public OrgPage(UserType userType) {
        this.userType = userType;
    }

    public OrgPage(IModel<SecondaryOrg> secondaryOrgModel) {
        this.uniqueId = secondaryOrgModel.getObject().getId();
        this.secondaryOrgModel = secondaryOrgModel == null?createSecondaryOrg():secondaryOrgModel;
    }

    public OrgPage (){}

    public OrgPage(PageParameters parameters) {
        filterCriteriaModel = getOrgListFilterCriteria();
        uniqueId = parameters.get("uniqueID")==null?0L:parameters.get("uniqueID").toLong();
        secondaryOrgModel = Model.of(loadExistingSecondaryOrg());
        filterCriteriaModel.getObject().withSecondaryOrg(loadExistingSecondaryOrg());
    }

    protected abstract void doSave();

    protected abstract Component createDetailsPanel(String id);

    protected SecondaryOrgFormAddressPanel addressPanel;
    protected SecondaryOrgFormReportImagePanel reportImagePanel;
    protected Component detailsPanel;

    protected SecondaryOrg loadExistingSecondaryOrg() {
        return orgService.getSecondaryOrg(uniqueId);
    }

    protected UploadedImage getReportImage() {
        return new UploadedImage();
    }

    protected void saveSignatureFile(UploadedImage reportImage) {
        new FileSystemSecondaryOrgReportFileProcessor(secondaryOrgModel.getObject()).process(reportImage);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        country = CountryList.getInstance().getCountryByFullName(getPrimaryOrg().getDefaultTimeZone());
        region = CountryList.getInstance().getRegionByFullId(getPrimaryOrg().getDefaultTimeZone());
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddSecondaryOrgForm("addSecondaryOrgForm"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    protected Model<OrgListFilterCriteria> getOrgListFilterCriteria() {
        return Model.of(new OrgListFilterCriteria(false));
    }

    @Override
    protected void addNavBar(String navBarId) {
        OrgListFilterCriteria criteria = new OrgListFilterCriteria(filterCriteriaModel.getObject());
        Long activeOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly(false));
        Long archivedOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly());
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeOrgCount)).page(OrgsListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedOrgCount)).page(ArchivedOrgsListPage.class).build(),
                aNavItem().label("nav.add").page(EditOrgPage.class).onRight().build()
        ));
    }

    protected SecondaryOrgFormDetailsPanel getSecondaryOrgFormDetailsPanel() {
        return (SecondaryOrgFormDetailsPanel) detailsPanel;
    }

    protected IModel<SecondaryOrg> createSecondaryOrg() {
        SecondaryOrg newSecondaryOrg = new SecondaryOrg();
        newSecondaryOrg.setTenant(getTenant());
        newSecondaryOrg.setModifiedBy(getCurrentUser());
        newSecondaryOrg.setCreatedBy(getCurrentUser());
        newSecondaryOrg.setPrimaryOrg(getPrimaryOrg());
        newSecondaryOrg.setDefaultTimeZone(getPrimaryOrg().getDefaultTimeZone());
        return Model.of(newSecondaryOrg);
    }

    protected SecondaryOrg create() {
        SecondaryOrg newSeconadryOrg = secondaryOrgModel.getObject();
        newSeconadryOrg.setName(getPrimaryOrg().getDisplayName());

        orgService.create(newSeconadryOrg);

        UploadedImage secondaryOrgLogoImage = reportImagePanel.getUploadedImage();

        if(secondaryOrgLogoImage.isNewImage()) {
            saveSignatureFile(secondaryOrgLogoImage);
        }

        return newSeconadryOrg;
    }

    protected SecondaryOrg update() {
        SecondaryOrg secondaryOrg = secondaryOrgModel.getObject();

        UploadedImage secondaryOrgLogoImage = reportImagePanel.getUploadedImage();

        if(secondaryOrgLogoImage.isNewImage() || secondaryOrgLogoImage.isRemoveImage()) {
            saveSignatureFile(secondaryOrgLogoImage);
        }

        if(secondaryOrg.isArchived()) {
            secondaryOrg.activateEntity();
        }

        orgService.update(secondaryOrg);

        return secondaryOrg;
    }

    protected void addConfirmBehavior(SubmitLink submitLink) {}

    protected void onOwnerPicked(AjaxRequestTarget target) {}

    class AddSecondaryOrgForm extends Form {

        SubmitLink submitLink;

        public AddSecondaryOrgForm(String id) {
            super(id);

            add(reportImagePanel = new SecondaryOrgFormReportImagePanel("reportImagePanel", secondaryOrgModel, getReportImage()));

            secondaryOrgModel = secondaryOrgModel == null?createSecondaryOrg():secondaryOrgModel;

            add(addressPanel = new SecondaryOrgFormAddressPanel("addressPanel", Model.of(secondaryOrgModel.getObject().getAddressInfo())));

            add(detailsPanel = createDetailsPanel("detailsPanel"));

            add(new SecondaryOrgFormLocalizationPanel("localizationPanel", secondaryOrgModel));

            add(submitLink = new SubmitLink("save"));

            addConfirmBehavior(submitLink);

            add(new BookmarkablePageLink<OrgsListPage>("cancel", OrgsListPage.class));
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


