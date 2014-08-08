package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class UserNameLinkCell extends Panel {

    public UserNameLinkCell(String id, IModel<User> model) {
        super(id, model);
        NonWicketLink link;
        add(link = new NonWicketLink("viewLink", "viewUser.action?uniqueID=" + model.getObject().getId()));
        link.add(new Label("name", new PropertyModel<String>(model, "displayName")));
    }
}
