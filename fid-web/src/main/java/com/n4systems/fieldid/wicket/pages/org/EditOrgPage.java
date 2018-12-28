package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.InternalOrgFormDetailsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.image.UploadedImage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EditOrgPage extends OrgPage {

    @SpringBean
    private S3Service s3Service;

    private Long previousOwnerId;

    public EditOrgPage(IModel<InternalOrg> internalOrg) {
        super(internalOrg);
    }

    public EditOrgPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        previousOwnerId = internalOrg.getObject().getOwner().getId();
    }

    @Override
    protected void doSave() {
        super.update();
        setResponsePage(OrgsListPage.class);
    }

    @Override
    protected void onOwnerPicked(AjaxRequestTarget target) {
        if (isOwnerChanged(internalOrg.getObject()) ) {
            target.appendJavaScript("confirmationRequired = true");
        } else {
            target.appendJavaScript("confirmationRequired = false");
        }
    }

    @Override
    protected void addConfirmBehavior(SubmitLink submitLink) {
        submitLink.add(new ConfirmBehavior(new FIDLabelModel("message.change_user_owner")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnLoadJavaScript("confirmationRequired = false");
    }

    @Override
    protected Component createDetailsPanel(String id) {
        return new InternalOrgFormDetailsPanel(id, internalOrg);
    }

    @Override
    protected UploadedImage getReportImage() {
        File reportImage;
        if (internalOrg.getObject().isPrimary()) reportImage = PathHandler.getReportImage((PrimaryOrg) internalOrg.getObject());
        else reportImage = PathHandler.getReportImage((SecondaryOrg) internalOrg.getObject());
        UploadedImage uploadedImage = new UploadedImage();

        if (reportImage.exists()) {
            uploadedImage.setImage(reportImage);
            uploadedImage.setUploadDirectory(reportImage.getPath());
        } else if(s3Service.isCertificateLogoExists(internalOrg.getObject().getId(), internalOrg.getObject().isPrimary())){
            reportImage = s3Service.downloadInternalOrgLogoImage(internalOrg.getObject());
            uploadedImage.setImage(reportImage);
            uploadedImage.setUploadDirectory(reportImage.getPath());
        }

        return uploadedImage;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_organizational_units.singular"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        OrgListFilterCriteria criteria = new OrgListFilterCriteria(false);
        Long activeOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly(false));
        Long archivedOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly());

        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeOrgCount)).page(OrgsListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedOrgCount)).page(ArchivedOrgsListPage.class).build(),
                aNavItem().label("nav.edit").page(EditOrgPage.class).params(uniqueId(internalOrg.getObject().getId())).build(),
                aNavItem().label("nav.add").page(AddSecondaryOrgPage.class).onRight().build()
        ));
    }

    private Boolean isOwnerChanged(InternalOrg internalOrg) {
        return internalOrg.getOwner().getId() != previousOwnerId;
    }
}
