package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.InternalOrgFormAddressPanel;
import com.n4systems.fieldid.wicket.components.org.InternalOrgFormDetailsPanel;
import com.n4systems.fieldid.wicket.components.org.InternalOrgFormLocalizationPanel;
import com.n4systems.fieldid.wicket.components.org.InternalOrgFormReportImagePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.persistence.image.FileSystemInternalOrgReportFileProcessor;
import com.n4systems.util.persistence.image.UploadedImage;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
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
    protected OrgService orgService;

    private Long uniqueId;
    protected IModel<InternalOrg> internalOrg;
    protected IModel<OrgListFilterCriteria> filterCriteriaModel;
    protected Country country;
    protected Region region;
    protected InternalOrg organization;

    public OrgPage(IModel<InternalOrg> internalOrg) {
        this.uniqueId = internalOrg.getObject().getId();
        this.internalOrg = internalOrg == null?createSecondaryOrg():internalOrg;
    }

    public OrgPage (){}

    public OrgPage(PageParameters parameters) {
        filterCriteriaModel = getOrgListFilterCriteria();
        PrimaryOrg primaryOrg = getPrimaryOrg();
        uniqueId = parameters.get("uniqueID")==null?0L:parameters.get("uniqueID").toLong();
        if (primaryOrg.getId().equals(uniqueId)) {
            organization = primaryOrg;
        } else {
            organization = loadExistingSecondaryOrg();
        }

        internalOrg = Model.of(organization);
        filterCriteriaModel.getObject().withOrgFilter(organization);
    }

    protected abstract void doSave();

    protected abstract Component createDetailsPanel(String id);

    protected InternalOrgFormAddressPanel addressPanel;
    protected InternalOrgFormReportImagePanel reportImagePanel;
    protected Component detailsPanel;

    protected SecondaryOrg loadExistingSecondaryOrg() {
        return orgService.getSecondaryOrg(uniqueId);
    }

    protected UploadedImage getReportImage() {
        return new UploadedImage();
    }

    protected void saveInternalOrgLogoImageFile(UploadedImage reportImage) {
        new FileSystemInternalOrgReportFileProcessor(internalOrg.getObject()).process(reportImage);
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
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_organizational_units"));
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

    protected InternalOrgFormDetailsPanel getSecondaryOrgFormDetailsPanel() {
        return (InternalOrgFormDetailsPanel) detailsPanel;
    }

    protected IModel<InternalOrg> createSecondaryOrg() {
        SecondaryOrg newSecondaryOrg = new SecondaryOrg();
        newSecondaryOrg.setTenant(getTenant());
        newSecondaryOrg.setModifiedBy(getCurrentUser());
        newSecondaryOrg.setCreatedBy(getCurrentUser());
        newSecondaryOrg.setPrimaryOrg(getPrimaryOrg());
        newSecondaryOrg.setDefaultTimeZone(getPrimaryOrg().getDefaultTimeZone());
        return Model.of(newSecondaryOrg);
    }

    protected InternalOrg create() {
        SecondaryOrg secondaryOrg = (SecondaryOrg) internalOrg.getObject();
        secondaryOrg.setName(((SecondaryOrg) detailsPanel.getInnermostModel().getObject()).getName());

        orgService.save(secondaryOrg);

        UploadedImage orgLogoImage = reportImagePanel.getUploadedImage();

        if(orgLogoImage.isNewImage()) {
            saveInternalOrgLogoImageFile(orgLogoImage);
        }

        return secondaryOrg;
    }

    protected InternalOrg update() {
        InternalOrg org = internalOrg.getObject();

        UploadedImage internalOrgLogoImage = reportImagePanel.getUploadedImage();

        if(internalOrgLogoImage.isNewImage() || internalOrgLogoImage.isRemoveImage()) {
            saveInternalOrgLogoImageFile(internalOrgLogoImage);
        }

        if(org.isArchived()) {
            org.activateEntity();
        }

        orgService.update(org);

        return org;
    }

    protected void addConfirmBehavior(SubmitLink submitLink) {}

    protected void onOwnerPicked(AjaxRequestTarget target) {}

    class AddSecondaryOrgForm extends Form {

        SubmitLink submitLink;

        public AddSecondaryOrgForm(String id) {
            super(id);

            add(reportImagePanel = new InternalOrgFormReportImagePanel("reportImagePanel", internalOrg, getReportImage()));

            internalOrg = internalOrg == null?createSecondaryOrg():internalOrg;

            add(addressPanel = new InternalOrgFormAddressPanel("addressPanel", Model.of(internalOrg.getObject().getAddressInfo())));

            add(detailsPanel = createDetailsPanel("detailsPanel"));

            add(new InternalOrgFormLocalizationPanel("localizationPanel", internalOrg));

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


