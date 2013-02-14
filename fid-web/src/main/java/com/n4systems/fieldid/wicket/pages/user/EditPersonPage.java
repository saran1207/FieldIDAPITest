package com.n4systems.fieldid.wicket.pages.user;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class EditPersonPage extends UserPage{

    Long uniqueId;

    public EditPersonPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        user = Model.of(loadExistingPerson());
    }

    @Override
    protected User doSave() {
        User person = user.getObject();
        person.setTimeZoneID(country.getFullName(region));

        userService.update(person);

        return person;
    }

    private User loadExistingPerson() {
        return userService.getUser(uniqueId);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_users.singular"));
    }

}
