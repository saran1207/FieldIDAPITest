package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.security.UserType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class AddPersonPage extends UserPage {

    public AddPersonPage() {
        super(UserType.PERSON);
        userModel = createUser(UserType.PERSON);
    }

    @Override
    protected void doSave() {
        userService.create(userModel.getObject());
        setResponsePage(UsersListPage.class);
    }

    @Override
    protected Component createAccountPanel(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    @Override
    protected Component createPermissionsPanel(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.addperson"));
    }



}
