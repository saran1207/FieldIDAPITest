package com.n4systems.fieldid.wicket.components.org.people.table;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;

public class LastLoginCell extends Panel {

    @SpringBean
    UserService userService;

    public LastLoginCell(String id, IModel<User> model) {
        super(id, model);

        Date lastLogin = userService.getLastLogin(model.getObject().getId());

        add(new Label("lastLogin", new DayDisplayModel(Model.of(lastLogin)).includeTime()));

    }
}
