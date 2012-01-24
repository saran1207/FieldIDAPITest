package com.n4systems.fieldid.wicket.components.saveditems;

import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;
import java.util.Map;

public class ShareWithUsersFromOrgPanel extends Panel {

    private Map<String, Boolean> sharedUsersMap;

    public ShareWithUsersFromOrgPanel(String id, List<User> users, Map<String,Boolean> sharedUsersMap) {
        super(id);
        this.sharedUsersMap = sharedUsersMap;

        add(new ListView<User>("userList", users) {
            @Override
            protected void populateItem(ListItem<User> item) {
                Long userId = item.getModelObject().getId();
                String modelPropertyPath = "sharedUsersMap[" + userId + "]";
                item.add(new CheckBox("shareWithThisUser", new PropertyModel<Boolean>(ShareWithUsersFromOrgPanel.this, modelPropertyPath)));
                item.add(new Label("userName", new PropertyModel<String>(item.getModel(), "fullName")));
            }
        });

    }

}
