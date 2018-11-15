package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EditPersonPage extends UserPage{

    public EditPersonPage(PageParameters parameters) {
        super(parameters);
    }

    public EditPersonPage(IModel<User> userModel) {
        super(userModel);
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
        UserListFilterCriteria criteria = new UserListFilterCriteria(false);
        Long activeUserCount = userService.countUsers(criteria.withArchivedOnly(false));
        Long archivedUserCount = userService.countUsers(criteria.withArchivedOnly());
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeUserCount)).page(UsersListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedUserCount)).page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.view").page(ViewUserPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.edit").page(EditPersonPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label(new FIDLabelModel("nav.import_export")).page(UserImportPage.class).onRight().build()
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
