package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.wicket.components.timezone.TimeZoneSelectorPanel;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class UserFormLocalizationPanel extends Panel {
    public UserFormLocalizationPanel(String id, IModel<User> user) {
        super(id, user);
        add(new TimeZoneSelectorPanel("timeZoneContainer", new PropertyModel<String>(user, "timeZoneID")));
    }
}
