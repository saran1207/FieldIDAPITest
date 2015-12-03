package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormPermissionsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.UserType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AddCustomerUserPage extends UserPage {

    @SpringBean
    private OrgService orgService;

    private BaseOrg customerOrg;

    public AddCustomerUserPage(PageParameters parameters) {
        super(UserType.READONLY);
        userModel = createUser(userType);

        Long customerId = parameters.get("customerId").toLong();

        customerOrg = orgService.findById(customerId);

        userModel.getObject().setOwner(customerOrg);
    }

    @Override
    protected void doSave() {
        create();
        redirect("/customersUsers.action?uniqueID=" + customerOrg.getId());
    }

    @Override
    protected Component createAccountPanel(String id) {
        return new UserFormAccountPanel(id, userModel);
    }

    @Override
    protected Component createPermissionsPanel(String id) {
        return new UserFormPermissionsPanel(id, userModel);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.customer_users", userType.getLabel()));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all")).page("customerList.action").build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived")).page("archivedCustomerList.action").build(),
                aNavItem().label(new FIDLabelModel("nav.view")).page("customerShow.action?uniqueID=" + customerOrg.getId()).build(),
                aNavItem().label(new FIDLabelModel("nav.edit")).page("customerEdit.action?uniqueID=" + customerOrg.getId()).build(),
                aNavItem().label(new FIDLabelModel("nav.divisions")).page("divisions.action?customerId=" + customerOrg.getId()).build(),
                aNavItem().label(new FIDLabelModel("nav.users")).page("customersUsers.action?customerId=" + customerOrg.getId()).build(),
                aNavItem().label("nav.add").page("customerEdit.action").onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }
}
