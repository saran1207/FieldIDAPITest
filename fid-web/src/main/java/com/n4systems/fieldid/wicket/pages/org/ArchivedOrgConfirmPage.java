package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.orgs.*;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.orgs.secondaryorg.CustomerOrgByOwnerListLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByOwnerListLoader;
import com.n4systems.util.OrganizationalUnitRemovalSummary;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class ArchivedOrgConfirmPage extends FieldIDTemplatePage {

    @SpringBean
    protected UserService userService;
    @SpringBean
    protected OrgService orgService;
    @SpringBean
    protected PlaceService placeService;

    private static Logger logger = Logger.getLogger(ArchivedOrgConfirmPage.class);


    private Long uniqueId;
    protected IModel<SecondaryOrg> secondaryOrgModel;
    protected IModel<OrgListFilterCriteria> filterCriteriaModel;
    private InternalOrg organization;
    private OrganizationalUnitRemovalSummary removalSummary;

    public ArchivedOrgConfirmPage(IModel<SecondaryOrg> secondaryOrgModel) {
        this.uniqueId = secondaryOrgModel.getObject().getId();
        this.secondaryOrgModel = secondaryOrgModel;
    }

    public ArchivedOrgConfirmPage(){}

    public ArchivedOrgConfirmPage(PageParameters parameters) {
        filterCriteriaModel = getOrgListFilterCriteria();
        PrimaryOrg primaryOrg = getPrimaryOrg();
        uniqueId = parameters.get("uniqueID")==null?0L:parameters.get("uniqueID").toLong();
        if (primaryOrg.getId().equals(uniqueId)) {
            organization = primaryOrg;
        } else {
            organization = loadExistingSecondaryOrg();
        }

        secondaryOrgModel = Model.of(loadExistingSecondaryOrg());
        filterCriteriaModel.getObject().withSecondaryOrg(loadExistingSecondaryOrg());
    }

    protected SecondaryOrg loadExistingSecondaryOrg() {
        return orgService.getSecondaryOrg(uniqueId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new Label("archiveSecondaryOrg", new FIDLabelModel("instruction.archive_secondary_org", organization.getName())));
        add(new ArchivedSecondaryOrgForm("archivedOrgConfirmForm"));
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
                aNavItem().label("nav.add").page(AddSecondaryOrgPage.class).onRight().build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.confirm_secondary_org_archive_no", organization.getName()));
    }

    protected void addConfirmBehavior(SubmitLink submitLink) {}

    class ArchivedSecondaryOrgForm extends Form {

        SubmitLink submitLink;

        public ArchivedSecondaryOrgForm(String id) {
            super(id);

            removalSummary = new OrganizationalUnitRemovalSummary(organization);
            updateRemovalSummary();
            add(new Label("customersToArchive", new FIDLabelModel("instruction.customers_to_archive",removalSummary.getCustomersToArchive())));
            add(new Label("divisionsToArchive", new FIDLabelModel("instruction.division_to_archive",removalSummary.getDivisionsToArchive())));
            add(new Label("usersToArchive", new FIDLabelModel("instruction.users_to_archive",removalSummary.getUsersToArchive())));

            add(submitLink = new SubmitLink("save"));

            addConfirmBehavior(submitLink);

            add(new BookmarkablePageLink<OrgsListPage>("cancel", OrgsListPage.class));
        }

        @Override
        protected void onSubmit() {
            doSave();
            FieldIDSession.get().info(new FIDLabelModel("message.archive_secondary_org").getObject());
        }

        private void updateRemovalSummary() {
            removalSummary.addUsers(getUsersByOwner(organization).size());

            List<CustomerOrg> customers = new CustomerOrgByOwnerListLoader(getSecurityFilter()).setOwner(organization).load();
            removalSummary.setCustomersToArchive(customers.size());

            for (CustomerOrg customer: customers) {
                removalSummary.addUsers(getUsersByOwner(customer).size());

                List<DivisionOrg> divisions = new DivisionOrgByCustomerListLoader(getSecurityFilter()).setCustomer(customer).load();
                removalSummary.addDivisions(divisions.size());

                for (DivisionOrg division: divisions) {
                    removalSummary.addUsers(getUsersByOwner(division).size());
                }
            }
        }

        private List<User> getUsersByOwner(BaseOrg owner) {
            return new UserByOwnerListLoader(getSecurityFilter()).owner(owner).load();
        }

    }

    protected void doSave() {
        doArchive();
        setResponsePage(OrgsListPage.class, PageParametersBuilder.uniqueId(secondaryOrgModel.getObject().getId()));
    }

    public String doArchive() {
        return activateSecondaryOrg(false);
    }

    public String doUnarchive() {
        return activateSecondaryOrg(true);
    }

    public String activateSecondaryOrg(boolean active) {
        if(organization == null) {
            return "ERROR";
        }
        try {
            if (!active) {
                placeService.archive(organization);
            } else {
                placeService.unarchive(organization);
            }
        } catch (Exception e) {
            logger.error("Failed updating customer", e);
            return "ERROR";
        }

        return "SUCCESS";
    }


    public PrimaryOrg getPrimaryOrg() {
        return getSecurityGuard().getPrimaryOrg();
    }

}


