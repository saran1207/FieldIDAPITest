package com.n4systems.fieldid.wicket.components.org.people.table;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class UserIdLinkCell extends Panel {

    public UserIdLinkCell(String id, IModel<User> model) {
        super(id, model);
        NonWicketLink link;
        add(link = new NonWicketLink("viewLink", "viewUser.action?uniqueID=" + model.getObject().getId()));
        link.add(new Label("userId", new PropertyModel<String>(model, "userID")));
    }
}
