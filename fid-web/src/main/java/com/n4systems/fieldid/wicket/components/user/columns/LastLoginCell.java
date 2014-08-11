package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.TimeZone;

public class LastLoginCell extends Panel {

    public LastLoginCell(String id, IModel<User> model) {
        super(id, model);
        TimeZone timeZone = FieldIDSession.get().getSessionUser().getTimeZone();
        add(new Label("lastLogin", new DayDisplayModel(Model.of(model.getObject().getLastLogin())).includeTime().withTimeZone(timeZone)));
    }

}
