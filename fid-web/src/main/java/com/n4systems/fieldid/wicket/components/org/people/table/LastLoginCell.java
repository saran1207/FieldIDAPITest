package com.n4systems.fieldid.wicket.components.org.people.table;

import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LastLoginCell extends Panel {

    public LastLoginCell(String id, IModel<User> model) {
        super(id, model);
        add(new Label("lastLogin", new DayDisplayModel(Model.of(model.getObject().getLastLogin())).includeTime()));
    }

}
