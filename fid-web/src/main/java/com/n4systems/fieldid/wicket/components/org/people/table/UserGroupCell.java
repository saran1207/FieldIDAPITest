package com.n4systems.fieldid.wicket.components.org.people.table;

import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class UserGroupCell extends Panel {

    public UserGroupCell(String id, IModel<User> model) {
        super(id, model);

        String groupsStr = "";
        for (UserGroup group : model.getObject().getGroups()) {
            groupsStr += group.getDisplayName() + ", ";
        }
        if(groupsStr.isEmpty())
            add(new Label("groups"));
        else
            add(new Label("groups", groupsStr.substring(0, groupsStr.length()-2)));


    }
}
