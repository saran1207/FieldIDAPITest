package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EditPersonPage extends UserPage{

    public EditPersonPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void doSave() {
        userService.update(userModel.getObject());
        setResponsePage(ViewUserPage.class, PageParametersBuilder.uniqueId(userModel.getObject().getId()));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_users.singular"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(UsersListPage.class).build(),
                aNavItem().label("nav.view_all_archived").page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.view").page("viewUser.action").params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.edit").page(EditPersonPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }

    @Override
    protected Component createAccountPanel(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    @Override
    protected Component createPermissionsPanel(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

}
