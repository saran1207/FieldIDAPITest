package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormPermissionsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

public class AddUserPage extends UserPage {

    public AddUserPage(UserType userType) {
        super(userType);
        userModel = createUser(userType);
    }

    @Override
    protected User doSave() {
        return create();
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
        return new Label(labelId, new FIDLabelModel("label.add_x_user", userType.getLabel()));
    }
}
