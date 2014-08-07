package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.user.User;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ArchivedUsersListActionCell extends Panel {
    public ArchivedUsersListActionCell(String componentId, IModel<User> rowModel) {
        super(componentId, rowModel);

        User user = rowModel.getObject();
        AttributeModifier attributeModifier = new AttributeModifier("class", "btn-secondary");
        if (user.isEmployee()) {
            add(new NonWicketLink("unarchive", "employeeUserUnarchive.action?uniqueID=" + user.getId(), attributeModifier));
        } else if(user.isLiteUser()) {
            add(new NonWicketLink("unarchive", "liteUserUnarchive.action?uniqueID=" + user.getId(), attributeModifier));
        } else if(user.isReadOnly()) {
            add(new NonWicketLink("unarchive", "readOnlyUnarchive.action?uniqueID=" + user.getId(), attributeModifier));
        } else if(user.isPerson()) {
            add(new NonWicketLink("unarchive", "personUnarchive.action?uniqueID=" + user.getId(), attributeModifier));
        } else if(user.isUsageBasedUser()) {
            add(new NonWicketLink("unarchive", "usageBasedUnarchive.action?uniqueID=" + user.getId(), attributeModifier));
        } else {
            add(new Link("unarchive") { @Override public void onClick() { } }.setVisible(false));
        }

    }
}
