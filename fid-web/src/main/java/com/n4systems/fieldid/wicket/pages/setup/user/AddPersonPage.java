package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class AddPersonPage extends UserPage {

    public AddPersonPage() {
        user = createPersonUser();
    }

    @Override
    protected User doSave() {
        User person = user.getObject();
        userService.create(person);
        return person;
    }

    @Override
    protected Component createAccountPanel(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    @Override
    protected Component createPermissionsPanel(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    private IModel<User> createPersonUser() {
        User person = new User();
        person.setTenant(getTenant());
        person.setRegistered(true);
        person.setModifiedBy(getCurrentUser());
        person.setCreatedBy(getCurrentUser());
        person.setUserType(UserType.PERSON);
        person.setTimeZoneID(getSessionUser().getOwner().getInternalOrg().getDefaultTimeZone());
        return Model.of(person);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.addperson"));
    }



}
